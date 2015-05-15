/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;

import java.util.ArrayList;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Combo;

public class LinkToProfileAttributeWizard extends AbstractSpagoBIModelWizard {

	EditingDomain editingDomain;
	
	SimpleBusinessColumn simpleBusinessColumn;
	IWizardPage pageOne;
	String projectName;
	
	
	public LinkToProfileAttributeWizard(SimpleBusinessColumn simpleBusinessColumn, EditingDomain editingDomain, ISpagoBIModelCommand command){
		super(editingDomain, command);
		this.setWindowTitle("Link to profile attribute");
		this.setHelpAvailable(false);	
		this.simpleBusinessColumn = simpleBusinessColumn;
		
	}
	
	@Override
	public void addPages() {
		pageOne = new LinkToProfileAttributeWizardPage("Link to attribute page one",simpleBusinessColumn);
		((LinkToProfileAttributeWizardPage)pageOne).setProjectName(projectName);
		addPage( pageOne );
	}
	
	public CommandParameter getCommandInputParameter(){
		LinkToProfileAttributeWizardPage wizardPage = (LinkToProfileAttributeWizardPage)pageOne;
		
		Combo attributesCombo = wizardPage.getAttributesCombo();
		int index = attributesCombo.getSelectionIndex();

		String selected = null;
		if(index != -1 && index != 0){
			selected = attributesCombo.getItem(index);
		}
		else{
			selected = null;
		}
		
		return new CommandParameter(simpleBusinessColumn, null, selected, new ArrayList<Object>());
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public EditingDomain getEditingDomain() {
		return editingDomain;
	}

	public void setEditingDomain(EditingDomain editingDomain) {
		this.editingDomain = editingDomain;
	}

}

