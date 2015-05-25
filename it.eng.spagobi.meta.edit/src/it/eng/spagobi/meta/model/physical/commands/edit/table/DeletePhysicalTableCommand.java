/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.physical.commands.edit.table;

import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalForeignKey;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.PhysicalPrimaryKey;
import it.eng.spagobi.meta.model.physical.PhysicalTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class DeletePhysicalTableCommand extends AbstractSpagoBIModelEditCommand {

	PhysicalModel model;
	PhysicalTable physicalTable;

	PhysicalPrimaryKey removedPrimaryKey;
	List<PhysicalForeignKey> removedForeignKeys;

	private static Logger logger = LoggerFactory.getLogger(DeletePhysicalTableCommand.class);

	public DeletePhysicalTableCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.physical.commands.edit.table.delete.label", "model.physical.commands.edit.table.delete.description",
				"model.business.commands.edit.table.delete", domain, parameter);
	}

	public DeletePhysicalTableCommand(EditingDomain domain) {
		this(domain, null);
	}

	protected PhysicalTable getPhysicalTable() {
		if (physicalTable == null)
			physicalTable = (PhysicalTable) parameter.getOwner();
		return physicalTable;
	}

	@Override
	public void execute() {

		model = getPhysicalTable().getModel();
		removedPrimaryKey = getPhysicalTable().getPrimaryKey();
		if (removedPrimaryKey != null) {
			model.getPrimaryKeys().remove(removedPrimaryKey);
		}
		removedForeignKeys = getPhysicalTable().getForeignKeysInvolvingTable();
		model.getForeignKeys().removeAll(removedForeignKeys);
		model.getTables().remove(getPhysicalTable());

		executed = true;
	}

	@Override
	public void undo() {

		model.getTables().add(getPhysicalTable());
		model.getForeignKeys().addAll(removedForeignKeys);
		if (removedPrimaryKey != null) {
			model.getPrimaryKeys().add(removedPrimaryKey);
		}

	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if (model != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(model);
		}
		return affectedObjects;
	}

}
