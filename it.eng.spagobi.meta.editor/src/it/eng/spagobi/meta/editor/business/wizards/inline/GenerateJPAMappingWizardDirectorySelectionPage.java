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

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.osgi.service.datalocation.Location;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * @author cortella
 * 
 */
public class GenerateJPAMappingWizardDirectorySelectionPage extends WizardPage {

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();

	private Text textDirectory;
	private Text txtSchemaName;
	private Text txtCatalogName;
	private Text txtModelName;

	private String schemaName;
	private String catalogName;
	private String modelName;

	private final String oldSchemaName;
	private final String oldCatalogName;
	private final String oldModelName;

	/**
	 * @param pageName
	 */
	protected GenerateJPAMappingWizardDirectorySelectionPage(String pageName, String oldSchemaName, String oldCatalogName, String oldModelName) {
		super(pageName);
		setTitle(RL.getString("business.editor.wizard.generatemapping.title"));
		setDescription(RL.getString("business.editor.wizard.generatemapping.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.createDatamart"));
		if (image != null)
			setImageDescriptor(image);
		this.oldSchemaName = oldSchemaName;
		this.oldModelName = oldModelName;
		this.oldCatalogName = oldCatalogName;
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);
		setControl(container);
		container.setLayout(new GridLayout(6, false));

		Label lblDirectory = new Label(container, SWT.NONE);
		lblDirectory.setText(RL.getString("business.editor.wizard.generatemapping.label"));

		// Input text box
		textDirectory = new Text(container, SWT.BORDER);
		GridData gd_textDirectory = new GridData(GridData.FILL_HORIZONTAL);
		gd_textDirectory.horizontalSpan = 4;
		textDirectory.setLayoutData(gd_textDirectory);
		Location location = Platform.getInstanceLocation();
		if (location != null) {
			textDirectory.setText(location.getURL().getPath().substring(1));
		} else
			textDirectory.setText("D:\\Programmi\\eclipse\\helios-eclipse-3.6.0\\runtime-EclipseApplication\\TestOda\\mappings");

		// Browse button to select directory
		Button buttonBrowse = new Button(container, SWT.PUSH);
		buttonBrowse.setText(RL.getString("business.editor.wizard.generatemapping.browsebutton"));
		buttonBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				DirectoryDialog dlg = new DirectoryDialog(new Shell());

				// Set the initial filter path according
				// to anything they've selected or typed in
				dlg.setFilterPath(textDirectory.getText());

				dlg.setText(RL.getString("business.editor.wizard.generatemapping.directoryselection"));
				dlg.setMessage(RL.getString("business.editor.wizard.generatemapping.directoryselection"));

				// Calling open() will open and run the dialog.
				// It will return the selected directory, or
				// null if user cancels
				String dir = dlg.open();
				if (dir != null) {
					// Set the text box to the new selection
					textDirectory.setText(dir);
				}

				checkPageComplete();
			}
		});

		createModelName(container);
		createSchemaName(container);
		createCatalogName(container);

	}

	private void createModelName(Composite container) {

		Label lbtModelName = new Label(container, SWT.NONE);
		lbtModelName.setText("Model Name");

		GridData dataModelName = new GridData();
		dataModelName.grabExcessHorizontalSpace = true;
		dataModelName.horizontalAlignment = GridData.FILL;

		txtModelName = new Text(container, SWT.BORDER);
		txtModelName.setLayoutData(dataModelName);
		if (oldModelName != null) {
			txtModelName.setText(oldModelName);
		} else {
			txtModelName.setEnabled(false);
		}

		// empty labels to fill
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	private void createSchemaName(Composite container) {

		Label lbtSchemaName = new Label(container, SWT.NONE);
		lbtSchemaName.setText("Schema Name");

		GridData dataFirstName = new GridData();
		dataFirstName.grabExcessHorizontalSpace = true;
		dataFirstName.horizontalAlignment = GridData.FILL;

		txtSchemaName = new Text(container, SWT.BORDER);
		txtSchemaName.setLayoutData(dataFirstName);
		if (oldSchemaName != null) {
			txtSchemaName.setText(oldSchemaName);
		} else {
			txtSchemaName.setEnabled(false);
		}

		// empty labels to fill
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	private void createCatalogName(Composite container) {

		Label lbtCatalogName = new Label(container, SWT.NONE);
		lbtCatalogName.setText("Catalog Name");

		GridData dataCatalogName = new GridData();
		dataCatalogName.grabExcessHorizontalSpace = true;
		dataCatalogName.horizontalAlignment = GridData.FILL;

		txtCatalogName = new Text(container, SWT.BORDER);
		txtCatalogName.setLayoutData(dataCatalogName);
		if (oldCatalogName != null) {
			txtCatalogName.setText(oldCatalogName);
		} else {
			txtCatalogName.setEnabled(false);
		}

		// empty labels to fill
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
		new Label(container, SWT.NONE);
	}

	private void checkPageComplete() {
		if (textDirectory.getText().length() > 0) {
			this.setPageComplete(true);
		} else {
			this.setPageComplete(false);
		}
	}

	public String getSelectedDirectory() {
		return textDirectory.getText();
	}

	public String getSchemaName() {
		return txtSchemaName.getText();
	}

	public String getCatalogName() {
		return txtCatalogName.getText();
	}

	public String getModelName() {
		return txtModelName.getText();
	}

}
