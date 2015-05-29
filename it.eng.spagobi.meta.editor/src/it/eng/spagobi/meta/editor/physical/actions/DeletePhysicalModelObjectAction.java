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
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.BusinessViewInnerJoinRelationship;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.table.DeleteBusinessTableCommand;
import it.eng.spagobi.meta.model.business.commands.edit.table.RemoveColumnsFromBusinessTable;
import it.eng.spagobi.meta.model.business.commands.edit.view.DeleteBusinessViewPhysicalTableCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.physical.commands.edit.table.DeletePhysicalTableCommand;
import it.eng.spagobi.meta.model.physical.commands.edit.table.RemoveColumnsFromPhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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
	Set<ModelObject> businessObjectsToDelete = new LinkedHashSet<ModelObject>(1);

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
		businessObjectsToDelete = new LinkedHashSet<ModelObject>();
		if (selection != null && !selection.isEmpty()) {
			PhysicalModelReferencesFinder referenceFinder = new PhysicalModelReferencesFinder();

			Iterator it = selection.iterator();
			while (it.hasNext()) {
				Object o = it.next();
				Set<ModelObject> businessObjectsRelated = new LinkedHashSet<ModelObject>();
				if ((o instanceof PhysicalTable) || (o instanceof PhysicalColumn)) {
					businessObjectsRelated = referenceFinder.getDistinctBusinessObjects((ModelObject) o);
					businessObjectsToDelete.addAll(businessObjectsRelated);

				}
				// remove physical table
				if (o instanceof PhysicalTable) {
					CommandParameter parameter = new CommandParameter(o);
					removeCommand = new DeletePhysicalTableCommand(domain, parameter);
					removeTableCommands.add(removeCommand);
					// remove related business tables
					for (ModelObject businessObjectToDelete : businessObjectsRelated) {
						if (businessObjectToDelete instanceof BusinessTable) {
							CommandParameter commandParameter = new CommandParameter(businessObjectToDelete);
							Command removeCommand = new DeleteBusinessTableCommand(domain, commandParameter);
							removeTableCommands.add(removeCommand);
						} else if (businessObjectToDelete instanceof BusinessView) {
							PhysicalTable physicalTable = ((PhysicalTable) o);
							BusinessView businessView = (BusinessView) businessObjectToDelete;
							if (businessView.getPhysicalTables().contains(physicalTable)) {
								CommandParameter commandParameter = new CommandParameter(businessView, null, physicalTable, null);
								removeCommand = new DeleteBusinessViewPhysicalTableCommand(domain, commandParameter);
								removeTableCommands.add(removeCommand);
							}

						}
					}

				}
				// remove physical column
				else if (o instanceof PhysicalColumn) {
					PhysicalColumn physicalColumn = (PhysicalColumn) o;
					PhysicalTable physicalTable = physicalColumn.getTable();

					// remove related business columns
					for (ModelObject businessObjectToDelete : businessObjectsRelated) {
						if (businessObjectToDelete instanceof SimpleBusinessColumn) {
							SimpleBusinessColumn businessColumn = (SimpleBusinessColumn) businessObjectToDelete;
							BusinessColumnSet businessColumnSet = businessColumn.getTable();
							if (!(businessColumnSet instanceof BusinessView)) {
								Collection<PhysicalColumn> relatedPhysicalColumns = new ArrayList<PhysicalColumn>();
								relatedPhysicalColumns.add(businessColumn.getPhysicalColumn());
								CommandParameter commandParameter = new CommandParameter(businessColumnSet, null, relatedPhysicalColumns, null);
								Command removeCommand = new RemoveColumnsFromBusinessTable(domain, commandParameter);
								removeColumnCommands.add(removeCommand);
							} else {
								// check the delete of business column inside a BusinessView
								// check if the columns is used in a BusinessViewInnerJoinRelationship, otherwise we can delete the column
								BusinessView businessView = (BusinessView) businessColumnSet;
								List<BusinessViewInnerJoinRelationship> innerRelationships = businessView.getJoinRelationships();
								boolean canBeDeleted = true;
								for (BusinessViewInnerJoinRelationship innerRelationship : innerRelationships) {
									if (innerRelationship.getSourceColumns().contains(physicalColumn)
											|| innerRelationship.getDestinationColumns().contains(physicalColumn)) {
										canBeDeleted = false;
										break;
									}
								}
								if (canBeDeleted) {
									// delete just the single business column from the business view
									Collection<PhysicalColumn> relatedPhysicalColumns = new ArrayList<PhysicalColumn>();
									relatedPhysicalColumns.add(businessColumn.getPhysicalColumn());
									CommandParameter commandParameter = new CommandParameter(businessColumnSet, null, relatedPhysicalColumns, null);
									Command removeCommand = new RemoveColumnsFromBusinessTable(domain, commandParameter);
									removeColumnCommands.add(removeCommand);
								} else {
									// Business Column used in an inner join relationship
									// remove the physical table corresponding to the business column
									if (businessView.getPhysicalTables().contains(physicalTable)) {
										PhysicalTable businessColumnPhysicalTable = businessColumn.getPhysicalColumn().getTable();
										CommandParameter commandParameter = new CommandParameter(businessView, null, businessColumnPhysicalTable, null);
										removeCommand = new DeleteBusinessViewPhysicalTableCommand(domain, commandParameter);
										removeColumnCommands.add(removeCommand);
									}
								}

							}

						}
					}
					// The remove the physical Column

					Collection<PhysicalColumn> physicalColumns = new ArrayList<PhysicalColumn>();
					physicalColumns.add(physicalColumn);
					CommandParameter parameter = new CommandParameter(physicalTable, null, physicalColumns, null);
					removeCommand = new RemoveColumnsFromPhysicalTable(domain, parameter);
					removeColumnCommands.add(removeCommand);

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
				removeCommand = new CompoundCommand(removeCommands.size() - 1, "Delete physical objects", "Delete physical objexts", removeCommands);
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
