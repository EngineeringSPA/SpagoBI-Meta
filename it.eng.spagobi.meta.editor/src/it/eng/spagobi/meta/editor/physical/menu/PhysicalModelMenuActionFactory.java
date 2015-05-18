/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.physical.menu;

import it.eng.spagobi.meta.editor.business.actions.AddBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.AddCalculatedFieldAction;
import it.eng.spagobi.meta.editor.business.actions.AddEmptyBusinessTableAction;
import it.eng.spagobi.meta.editor.business.actions.AddIdentifierAction;
import it.eng.spagobi.meta.editor.business.actions.AddIncomeBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddOutcomeBusinessRelationshipAction;
import it.eng.spagobi.meta.editor.business.actions.AddToIdentifierAction;
import it.eng.spagobi.meta.editor.business.actions.CreateQueryAction;
import it.eng.spagobi.meta.editor.business.actions.EditBusinessColumnsAction;
import it.eng.spagobi.meta.editor.business.actions.EditBusinessViewInnerJoinRelationshipsAction;
import it.eng.spagobi.meta.editor.business.actions.GenerateJPAMappingAction;
import it.eng.spagobi.meta.editor.business.actions.RemoveFromIdentifierAction;
import it.eng.spagobi.meta.editor.olap.actions.CreateMondrianAction;
import it.eng.spagobi.meta.editor.olap.actions.EditHierarchiesAction;
import it.eng.spagobi.meta.editor.olap.actions.SetAttributeAction;
import it.eng.spagobi.meta.editor.olap.actions.SetCubeAction;
import it.eng.spagobi.meta.editor.olap.actions.SetDimensionAction;
import it.eng.spagobi.meta.editor.olap.actions.SetGenericAction;
import it.eng.spagobi.meta.editor.olap.actions.SetMeasureAction;
import it.eng.spagobi.meta.editor.physical.actions.UpdatePhysicalModelAction;
import it.eng.spagobi.meta.initializer.OlapModelInitializer;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.business.CalculatedBusinessColumn;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.olap.Cube;
import it.eng.spagobi.meta.model.phantom.provider.BusinessRootItemProvider;
import it.eng.spagobi.meta.model.phantom.provider.PhysicalRootItemProvider;
import it.eng.spagobi.meta.model.physical.provider.PhysicalTableItemProvider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.command.Command;
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
		
		if(selection.isEmpty())return actions;
		
		IStructuredSelection sselection = (IStructuredSelection) selection;
		List<?> list = sselection.toList();
		Object target = list.get(0);
		    
		if(target instanceof PhysicalRootItemProvider) {
	    	
	    	List updateActions = new ArrayList();
	    	updateActions.add(new UpdatePhysicalModelAction(activeEditorPart, selection));
	    	actions.put("Update", updateActions);

	    } else if (target instanceof PhysicalTableItemProvider){
	    	//do something...
	    }
		
		return actions;
	}
	


}
