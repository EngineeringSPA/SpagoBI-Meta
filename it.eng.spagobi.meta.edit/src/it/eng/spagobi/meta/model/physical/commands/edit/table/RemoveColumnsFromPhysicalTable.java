/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.physical.commands.edit.table;

import it.eng.spagobi.meta.initializer.BusinessModelInitializer;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO preserve column order upon undo
 * 
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class RemoveColumnsFromPhysicalTable extends AbstractSpagoBIModelEditCommand {

	BusinessModelInitializer initializer;

	// input values
	PhysicalTable physicalTable;
	Collection<PhysicalColumn> columnsToRemove;
	List<PhysicalColumn> columnsRemovedFromPrimaryKey;
	PhysicalPrimaryKey removedPrimaryKey;

	List<PhysicalForeignKey> removedForeignKeys;

	// cache edited columns (added and removed) for undo e redo
	List<PhysicalColumn> removedColumns;

	private static Logger logger = LoggerFactory.getLogger(RemoveColumnsFromPhysicalTable.class);

	public RemoveColumnsFromPhysicalTable(EditingDomain domain, CommandParameter parameter) {
		super("model.physical.commands.edit.table.removecolumns.label", "model.physical.commands.edit.table.removecolumns.description",
				"model.physical.commands.edit.table.removecolumns", domain, parameter);
		initializer = new BusinessModelInitializer();
	}

	public RemoveColumnsFromPhysicalTable(EditingDomain domain) {
		this(domain, null);
	}

	protected void clearCachedObjects() {
		removedColumns = new ArrayList<PhysicalColumn>();
		columnsRemovedFromPrimaryKey = new ArrayList<PhysicalColumn>();
		removedPrimaryKey = null;
		removedForeignKeys = new ArrayList<PhysicalForeignKey>();

	}

	@Override
	protected boolean prepare() {
		physicalTable = (PhysicalTable) parameter.getOwner();
		columnsToRemove = (Collection) parameter.getValue();
		return (physicalTable != null && columnsToRemove != null);
	}

	@Override
	public void execute() {

		clearCachedObjects();

		for (PhysicalColumn column : columnsToRemove) {

			updatePrimaryKey(column);
			updateForeignKeys(column);

			// remove
			physicalTable.getColumns().remove(column);
			removedColumns.add(column);
		}

		executed = true;
	}

	@Override
	public void undo() {
		for (PhysicalColumn column : removedColumns) {
			physicalTable.getColumns().add(column);
		}
		undoUpdateForeignKeys();
		undoUpdatePrimaryKey();

	}

	@Override
	public void redo() {
		execute();
	}

	private void updatePrimaryKey(PhysicalColumn physicalColumn) {

		if (physicalColumn.isPrimaryKey()) {
			PhysicalPrimaryKey primaryKey = physicalTable.getPrimaryKey();
			primaryKey.getColumns().remove(physicalColumn);
			columnsRemovedFromPrimaryKey.add(physicalColumn);

			if (primaryKey.getColumns().size() == 0) {
				physicalTable.getModel().getPrimaryKeys().remove(primaryKey);
				removedPrimaryKey = primaryKey;
			}
		}
	}

	private void undoUpdatePrimaryKey() {
		if (removedPrimaryKey != null) {
			physicalTable.getModel().getPrimaryKeys().add(removedPrimaryKey);
		}

		if (columnsRemovedFromPrimaryKey.size() > 0) {
			physicalTable.getPrimaryKey().getColumns().addAll(columnsRemovedFromPrimaryKey);
		}

	}

	private void updateForeignKeys(PhysicalColumn physicalColumn) {

		List<PhysicalForeignKey> foreignKeys;
		List<PhysicalForeignKey> removedForeignKeysAfterColumnDeletion = new ArrayList<PhysicalForeignKey>();

		foreignKeys = physicalTable.getForeignKeysInvolvingTable();

		for (PhysicalForeignKey foreignKey : foreignKeys) {
			List<PhysicalColumn> sourceColumns = foreignKey.getSourceColumns();
			List<PhysicalColumn> destinationColumns = foreignKey.getDestinationColumns();

			if (sourceColumns.contains(physicalColumn)) {
				foreignKey.getSourceColumns().remove(physicalColumn);
				removedForeignKeysAfterColumnDeletion.add(foreignKey);

			} else if (destinationColumns.contains(physicalColumn)) {
				foreignKey.getSourceColumns().remove(physicalColumn);
				removedForeignKeysAfterColumnDeletion.add(foreignKey);
			}

			if (foreignKey.getSourceColumns().isEmpty() || foreignKey.getDestinationColumns().isEmpty()) {
				removedForeignKeysAfterColumnDeletion.add(foreignKey);
			}
		}
		// remove inconsistent relationships
		removedForeignKeys.addAll(removedForeignKeysAfterColumnDeletion);
		physicalTable.getModel().getForeignKeys().removeAll(removedForeignKeysAfterColumnDeletion);
	}

	private void undoUpdateForeignKeys() {
		physicalTable.getModel().getForeignKeys().addAll(removedForeignKeys);

	}

	@Override
	public Collection<?> getAffectedObjects() {
		PhysicalTable businessTable = (PhysicalTable) parameter.getOwner();
		Collection affectedObjects = Collections.EMPTY_LIST;
		if (businessTable != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessTable);
		}
		return affectedObjects;
	}

	@Override
	public Collection<?> getResult() {
		// TODO the result here should change with operation type (execute = columns; undo = table)
		return getAffectedObjects();
	}

	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, title, message);
			}
		});
	}

}
