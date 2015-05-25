/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.editor.physical.actions;

import it.eng.spagobi.meta.editor.physical.dialogs.PhysicalObjectDeleteDialog;
import it.eng.spagobi.meta.initializer.utils.PhysicalModelReferencesFinder;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.physical.commands.edit.table.DeletePhysicalTableCommand;
import it.eng.spagobi.meta.model.physical.commands.edit.table.RemoveColumnsFromPhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.ui.action.DeleteAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class DeletePhysicalModelObjectAction extends DeleteAction {
	List<ModelObject> businessObjectsToDelete = new ArrayList<ModelObject>(1);

	public DeletePhysicalModelObjectAction(EditingDomain domain, boolean removeAllReferences) {
		super(domain, removeAllReferences);
	}

	public DeletePhysicalModelObjectAction(EditingDomain domain) {
		super(domain);
	}

	public DeletePhysicalModelObjectAction(boolean removeAllReferences) {
		super(removeAllReferences);
	}

	public DeletePhysicalModelObjectAction() {
		super();
	}

	@Override
	public void run() {
		IStructuredSelection selection = this.getStructuredSelection();
		// TODO: think about multi selection

		// Create confirmation Dialog
		PhysicalObjectDeleteDialog dialog = new PhysicalObjectDeleteDialog(businessObjectsToDelete);
		if (dialog.open() == Window.OK) {
			domain.getCommandStack().execute(command);
		}

	}

	Command removeCommand;

	@Override
	public Command createCommand(Collection<?> selection) {

		Command command = null;

		List<Command> removeTableCommands = new ArrayList<Command>();
		List<Command> removeColumnCommands = new ArrayList<Command>();

		if (selection != null && !selection.isEmpty()) {
			Iterator it = selection.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				PhysicalModelReferencesFinder referenceFinder = new PhysicalModelReferencesFinder();
				if ((o instanceof PhysicalTable) || (o instanceof PhysicalColumn)) {
					businessObjectsToDelete = referenceFinder.getBusinessObjects((ModelObject) o);

				}
				if (o instanceof PhysicalTable) {
					CommandParameter parameter = new CommandParameter(o);
					removeCommand = new DeletePhysicalTableCommand(domain, parameter);
					removeTableCommands.add(removeCommand);
					for (ModelObject businessObjectToDelete : businessObjectsToDelete) {
						if (businessObjectToDelete instanceof BusinessTable) {
							CommandParameter commandParameter = new CommandParameter(businessObjectToDelete);
							Command removeCommand = new DeleteBusinessTableCommand(domain, commandParameter);
							removeTableCommands.add(removeCommand);
						}
					}

				} else if (o instanceof PhysicalColumn) {
					PhysicalColumn physicalColumn = (PhysicalColumn) o;
					PhysicalTable physicalTable = physicalColumn.getTable();
					Collection<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
					physicalColumns.add(physicalColumn);
					CommandParameter parameter = new CommandParameter(physicalTable, null, physicalColumns, null);
					removeCommand = new RemoveColumnsFromPhysicalTable(domain, parameter);
					removeColumnCommands.add(removeCommand);

					// TODO: add remove of business columns
					// **** CONTINUARE DA QUI *********

				}
			}

			Command removeTablesCommand = null;
			if (!removeTableCommands.isEmpty()) {
				removeTablesCommand = new CompoundCommand(removeTableCommands.size() - 1, "Delete physical tables", "Delete physical tables",
						removeTableCommands);
			}

			Command removeColumnsCommand = null;
			if (!removeColumnCommands.isEmpty()) {
				removeColumnsCommand = new CompoundCommand(removeColumnCommands.size() - 1, "Delete physical columns", "Delete physical columns",
						removeColumnCommands);
			}

			removeCommand = null;

			if (removeTablesCommand != null && removeColumnsCommand != null) {
				List<Command> removeCommands = new ArrayList<Command>();
				removeCommands.add(removeColumnsCommand);
				removeCommands.add(removeTablesCommand);
				removeCommand = new CompoundCommand(removeCommands.size() - 1, "Delete", "Delete", removeCommands);
			} else if (removeTablesCommand != null && removeColumnsCommand == null) {
				removeCommand = removeTablesCommand;
			} else if (removeTablesCommand == null && removeColumnsCommand != null) {
				removeCommand = removeColumnsCommand;
			}

		}

		command = removeCommand;

		if (command == null) {
			command = super.createCommand(selection);
		}

		return command;
	}

}
