/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.actions;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.business.wizards.inline.LinkToProfileAttributeWizard;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.business.commands.edit.profileAttribute.LinkToProfileAttributeCommand;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;


/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class LinkToProfileAttributeAction extends AbstractSpagoBIModelAction {
	
	String projectName;
	
	ISpagoBIModelCommand performFinishCommand; 
	public LinkToProfileAttributeAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(LinkToProfileAttributeCommand.class, workbenchPart, selection);
		if (command instanceof ISpagoBIModelCommand)
			this.performFinishCommand = (ISpagoBIModelCommand)command;
	
		// get project name needed for connection
		IWorkbench wb = PlatformUI.getWorkbench();
		IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
		IWorkbenchPage pp = win.getActivePage();
		BusinessModelEditor bme = (BusinessModelEditor)pp.getActiveEditor();
		projectName = bme.getProjectName();
	
	}
	
	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		SimpleBusinessColumn simpleBusinessColumn = (SimpleBusinessColumn)owner;
		LinkToProfileAttributeWizard wizard = new LinkToProfileAttributeWizard( simpleBusinessColumn, editingDomain, (ISpagoBIModelCommand)command );
		wizard.setProjectName(projectName);
		wizard.setEditingDomain(editingDomain);
		WizardDialog dialog = new WizardDialog(new Shell(), wizard);
		dialog.create();
    	dialog.open();
	}

	/**
	 * @return the performFinishCommand
	 */
	public ISpagoBIModelCommand getPerformFinishCommand() {
		return performFinishCommand;
	}
	
}
