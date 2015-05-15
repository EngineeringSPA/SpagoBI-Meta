/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/

package it.eng.spagobi.meta.editor.business.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.SimpleBusinessColumn;
import it.eng.spagobi.studio.utils.bo.ProfileAttribute;
import it.eng.spagobi.studio.utils.exceptions.NoActiveServerException;
import it.eng.spagobi.studio.utils.services.SpagoBIServerObjectsFactory;

import java.rmi.RemoteException;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.PlatformUI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkToProfileAttributeWizardPage extends WizardPage {

	private Combo attributesCombo; 
	String projectName;
	SimpleBusinessColumn simpleBusinessColumn;
	
	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator(); 
	private static Logger logger = LoggerFactory.getLogger(LinkToProfileAttributeWizardPage.class);

	
	protected LinkToProfileAttributeWizardPage(String pageName, SimpleBusinessColumn simpleBusinessColumn) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.linktoprofileattribute.title"));
		setDescription(RL.getString("business.editor.wizard.linktoprofileattribute.description"));
		this.simpleBusinessColumn = simpleBusinessColumn;
	}

	@Override
	public void createControl(Composite parent) {
		
		
		SpagoBIServerObjectsFactory proxyServerObjects = null;
		try {
			proxyServerObjects = new SpagoBIServerObjectsFactory(projectName);

			ProfileAttribute[] attributes = proxyServerObjects.getServerDocuments().getAllAttributes(null);

			//Main composite
			Composite composite = new Composite(parent, SWT.NULL);
			GridLayout gl = new GridLayout();
			gl.numColumns = 2;
			gl.makeColumnsEqualWidth = true;
			composite.setLayout(gl);
			GridData gd2 = new GridData(GridData.FILL_BOTH);
			composite.setLayoutData(gd2);

			Label subTypeLabel = new Label(composite, SWT.NULL);
			subTypeLabel.setText("Select the optional profile attribute:");
			attributesCombo = new Combo(composite,  SWT.SIMPLE | SWT.DROP_DOWN | SWT.READ_ONLY);
			//attributesCombo.setLayoutData(composite);
			
			Object modPropO = simpleBusinessColumn.getProperties().get("structural.attribute");
			ModelProperty modProp = modPropO != null ? (ModelProperty)modPropO : null;
			String alreadySelected = modProp != null  ? modProp.getValue() : null;
			
			attributesCombo.add(" ");
			for (int i = 0; i < attributes.length; i++) {
				ProfileAttribute profAtt = attributes[i];
				attributesCombo.add(profAtt.getName());
				if(alreadySelected != null && alreadySelected.equals(profAtt.getName())){
					attributesCombo.select(i+1); // because there is initial empty string
				}
			}
	
			setControl(composite);
		} catch (NoActiveServerException e) {
			logger.error("No Server is defined active");			
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error in getting attributes", "No Server is defined active");
			return;
		} catch (RemoteException e) {
			logger.error("Attributes not retrieved. Check server is working");			
			MessageDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					"Error", "Attributes not retrieved. Check server is working");	
			return;
		}

	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Combo getAttributesCombo() {
		return attributesCombo;
	}


	
	
}
