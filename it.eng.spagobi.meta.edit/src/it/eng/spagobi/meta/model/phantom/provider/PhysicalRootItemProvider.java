/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.phantom.provider;

import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.commands.update.UpdatePhysicalModelCommand;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;

public class PhysicalRootItemProvider extends FolderItemProvider {

	public PhysicalRootItemProvider(AdapterFactory adapterFactory, Object parent, Collection children) {
		super(adapterFactory, parent, children);
	}

	@Override
	public Collection<?> getNewChildDescriptors(Object object, EditingDomain editingDomain, Object sibling) {
		PhysicalModel physicalModel = (PhysicalModel) parentObject;

		// Build the collection of new child descriptors.
		//
		Collection<Object> newChildDescriptors = new ArrayList<Object>();
		collectNewChildDescriptors(newChildDescriptors, object);

		return super.getNewChildDescriptors(physicalModel, editingDomain, sibling);
	}

	@Override
	public Command createCustomCommand(Object object, EditingDomain domain, Class<? extends Command> commandClass, CommandParameter commandParameter) {
		Command result;

		result = null;

		if (commandClass == UpdatePhysicalModelCommand.class) {
			result = new UpdatePhysicalModelCommand(domain, commandParameter);
		}

		return result;
	}
}
