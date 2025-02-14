/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.olap.commands.edit.dimension;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.olap.Dimension;
import it.eng.spagobi.meta.model.olap.OlapModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 * 
 */
public class CreateTemporalDimensionCommand extends AbstractSpagoBIModelEditCommand {

	OlapModelInitializer olapModelInitializer;

	BusinessColumnSet businessColumnSet;
	OlapModel olapModel;
	Dimension addedDimension;
	String originalTableType;
	Object removedPreviousObject;

	private static Logger logger = LoggerFactory.getLogger(CreateTemporalDimensionCommand.class);

	/**
	 * @param commandLabel
	 * @param commandDescription
	 * @param commandImage
	 * @param domain
	 * @param parameter
	 */
	public CreateTemporalDimensionCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.olap.commands.edit.temporaldimension.create.label", "model.olap.commands.edit.dimension.temporaldimension.description",
				"model.olap.commands.edit.dimension.create", domain, parameter);
		olapModelInitializer = new OlapModelInitializer();
	}

	public CreateTemporalDimensionCommand(EditingDomain domain) {
		this(domain, null);
	}

	@Override
	public void execute() {
		if (parameter.getValue() instanceof BusinessColumnSet) {
			olapModel = (OlapModel) parameter.getOwner();
			businessColumnSet = (BusinessColumnSet) parameter.getValue();
			// get the original Table Type Value for undo
			if (parameter.getFeature() != null) {
				// original type from the property view
				originalTableType = (String) parameter.getFeature();
			} else {
				originalTableType = businessColumnSet.getProperties().get("structural.tabletype").getValue();
			}
			// remove previous objects
			removedPreviousObject = olapModelInitializer.removeCorrespondingOlapObject(businessColumnSet);

			addedDimension = olapModelInitializer.addDimension(olapModel, businessColumnSet);

			// Set property tabletype = temporal dimension
			businessColumnSet.getProperties().get("structural.tabletype").setValue("temporal dimension");

			this.executed = true;
			logger.debug("Command [{}] executed succesfully", CreateTemporalDimensionCommand.class.getName());

		}

	}

	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if (olapModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(olapModel);
		}
		return affectedObjects;
	}

	@Override
	public void undo() {
		if ((removedPreviousObject != null) && (removedPreviousObject instanceof Cube)) {
			olapModelInitializer.addCube(olapModel, (Cube) removedPreviousObject);
		}
		olapModelInitializer.removeDimension(olapModel, addedDimension);
		businessColumnSet.getProperties().get("structural.tabletype").setValue(originalTableType);
	}

	@Override
	public void redo() {
		execute();
	}

}
