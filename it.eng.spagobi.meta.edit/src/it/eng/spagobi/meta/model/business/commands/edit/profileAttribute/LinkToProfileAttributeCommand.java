/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.model.business.commands.edit.profileAttribute;

import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Giulo Gavardi (giulio.gavardi@eng.it)
 *
 */
public class LinkToProfileAttributeCommand extends AbstractSpagoBIModelEditCommand {

	BusinessColumn businessColumn;
	
	private static Logger logger = LoggerFactory.getLogger(LinkToProfileAttributeCommand.class);
	
	
	public LinkToProfileAttributeCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.edit.identifier.linktoprofileattribute.label"
			 , "model.business.commands.edit.identifier.linktoprofileattribute.description"
			 , "model.business.commands.edit.identifier.linktoprofileattribute"
			 , domain, parameter);
	}
	
	public LinkToProfileAttributeCommand(EditingDomain domain) {
		this(domain, null);
	}
	
	@Override
	public void execute() {
		businessColumn = (BusinessColumn)parameter.getOwner();
		String valueSelected = null;
		if(parameter.getValue() != null){
			valueSelected = (String) parameter.getValue();
		}
		businessColumn.getProperties().get("structural.attribute").setValue(valueSelected);
		
		this.executed = true;
		logger.debug("Command [{}] executed succesfully", LinkToProfileAttributeCommand.class.getName());
	}
	
	
	
	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if(businessColumn != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(businessColumn.getTable());
		}
		return affectedObjects;
	}

	@Override
	public boolean canUndo() {
		return false;
	}
	
	
	
}
