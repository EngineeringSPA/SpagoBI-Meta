/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.editor.olap.actions;

import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.olap.OlapModel;
import it.eng.spagobi.meta.model.olap.commands.edit.dimension.CreateTimeDimensionCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author cortella
 * 
 */
public class SetTimeDimensionAction extends AbstractSpagoBIModelAction {

	private BusinessColumnSet businessColumnSet;
	public OlapModelInitializer olapModelInitializer = new OlapModelInitializer();

	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public SetTimeDimensionAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(CreateTimeDimensionCommand.class, workbenchPart, selection);

	}

	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			businessColumnSet = (BusinessColumnSet) owner;

			Model rootModel = businessColumnSet.getModel().getParentModel();
			OlapModel olapModel = rootModel.getOlapModels().get(0);

			CommandParameter commandParameter = new CommandParameter(olapModel, null, businessColumnSet, new ArrayList<Object>());
			if (editingDomain != null) {
				editingDomain.getCommandStack().execute(new CreateTimeDimensionCommand(editingDomain, commandParameter));
			}

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

}
