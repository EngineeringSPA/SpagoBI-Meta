/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.editor.physical.menu;

import it.eng.spagobi.meta.editor.physical.actions.UpdatePhysicalModelAction;
import it.eng.spagobi.meta.model.phantom.provider.PhysicalRootItemProvider;
import it.eng.spagobi.meta.model.physical.provider.PhysicalTableItemProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

/**
 * This factory generate actions used in contextual menu
 * 
 * @author Marco Cortella (marco.cortella@eng.it)
 */
public class PhysicalModelMenuActionFactory {

	public static Map<String, Collection<IAction>> getActions(IEditorPart activeEditorPart, Collection<?> descriptors, ISelection selection) {
		Map actions = new HashMap();

		if (selection.isEmpty())
			return actions;

		IStructuredSelection sselection = (IStructuredSelection) selection;
		List<?> list = sselection.toList();
		Object target = list.get(0);

		if (target instanceof PhysicalRootItemProvider) {

			List updateActions = new ArrayList();
			updateActions.add(new UpdatePhysicalModelAction(activeEditorPart, selection));
			actions.put("Update", updateActions);

		} else if (target instanceof PhysicalTableItemProvider) {
			// do something...
		}

		return actions;
	}

}
