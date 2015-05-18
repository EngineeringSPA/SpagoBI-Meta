/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.physical.menu;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.editor.business.actions.RefreshViewerAction;
import it.eng.spagobi.meta.editor.business.actions.ShowPropertiesViewAction;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class PhysicalModelMenuBarContributor {
	
	protected IMenuManager toolbarMainMenuManager;
	protected Map<String, PhysicalInlineMenuContributor> toolbarSubMenuManagers;
	
	protected RefreshViewerAction refreshViewerAction;
	protected ShowPropertiesViewAction showPropertiesViewAction;
	
	private static Logger logger = LoggerFactory.getLogger(PhysicalModelMenuBarContributor.class);
	private static IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();
	
	public Map<String, PhysicalInlineMenuContributor> getSubmenuManagers() {
		return toolbarSubMenuManagers;
	}

	public PhysicalModelMenuBarContributor(IMenuManager parentMenuManager, boolean insertAfter, String anchorMenuItemId) {
		
		toolbarMainMenuManager = new MenuManager(RL.getString("model.business.presentation.menu.metamodel"), "it.eng.spagobi.meta.model.physicalMenuID");
		toolbarSubMenuManagers = new HashMap();
		
		refreshViewerAction = new RefreshViewerAction();
		showPropertiesViewAction = new ShowPropertiesViewAction();
		
		// add the custom toolbar menu in the right place
		if(insertAfter == true) {
			parentMenuManager.insertAfter(anchorMenuItemId, toolbarMainMenuManager);
		} else {
			parentMenuManager.insertBefore(anchorMenuItemId, toolbarMainMenuManager);
		}
		
		// divide the custom menu into parts
		toolbarMainMenuManager.add(new Separator("settings"));
		toolbarMainMenuManager.add(new Separator("actions"));
		toolbarMainMenuManager.add(new Separator("additions"));
		toolbarMainMenuManager.add(new Separator("additions-end"));
		
		// Force an update because Eclipse hides empty menus now.
		toolbarMainMenuManager.addMenuListener
			(new IMenuListener() {
				 public void menuAboutToShow(IMenuManager menuManager) {
					 menuManager.updateAll(true);
				 }
			 });
		
		// these actions are not sensitive to selection
		addGlobalActions();
	
	}
	
	protected void addGlobalActions() {
		toolbarMainMenuManager.insertAfter("additions-end", new Separator("ui-actions"));
		toolbarMainMenuManager.insertAfter("ui-actions", showPropertiesViewAction);

		refreshViewerAction.setEnabled(refreshViewerAction.isEnabled());		
		toolbarMainMenuManager.insertAfter("ui-actions", refreshViewerAction);
	}
	
	public void setActiveEditor(IEditorPart activeEditorPart) {
		refreshViewerAction.setActiveEditorPart(activeEditorPart);
		showPropertiesViewAction.setActiveEditorPart(activeEditorPart);
	}
	
	public void selectionChanged(IEditorPart activeEditorPart, SelectionChangedEvent event) {
		
		Collection<?> newChildDescriptors = null;
		Collection<?> newSiblingDescriptors = null;
		
		ISelection selection = event.getSelection();
		if (selection instanceof IStructuredSelection && ((IStructuredSelection)selection).size() == 1) {
			Object object = ((IStructuredSelection)selection).getFirstElement();

			EditingDomain domain = ((IEditingDomainProvider)activeEditorPart).getEditingDomain();

			newChildDescriptors = domain.getNewChildDescriptors(object, null);
			newSiblingDescriptors = domain.getNewChildDescriptors(null, object);
		}
		
		depopulateAllSubMenus();
		
		Map<String, Collection<IAction>> actions = PhysicalModelMenuActionFactory.getActions(activeEditorPart, newChildDescriptors, selection);
		
		Iterator<String> it = actions.keySet().iterator();
		while(it.hasNext()) {
			String submenuName = it.next();
			PhysicalInlineMenuContributor contextualMenuManager = toolbarSubMenuManagers.get(submenuName);
			if(contextualMenuManager == null) {
				contextualMenuManager = new PhysicalInlineMenuContributor(toolbarMainMenuManager, submenuName);
				toolbarSubMenuManagers.put(submenuName, contextualMenuManager);
			}
			
			contextualMenuManager.setActions(actions.get(submenuName));
		}
	}
	
	protected void depopulateAllSubMenus() {
		Iterator<String> it = toolbarSubMenuManagers.keySet().iterator();
		while(it.hasNext()) {
			String submenuName = it.next();
			PhysicalInlineMenuContributor contextualMenuManager = toolbarSubMenuManagers.get(submenuName);
			contextualMenuManager.depopulateManager();
		}
		
	}
}
