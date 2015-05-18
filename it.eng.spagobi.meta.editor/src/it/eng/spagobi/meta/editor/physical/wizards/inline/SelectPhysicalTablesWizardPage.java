/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.editor.physical.wizards.inline;

import it.eng.spagobi.commons.resource.IResourceLocator;
import it.eng.spagobi.meta.editor.SpagoBIMetaEditorPlugin;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.net.URL;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

public class SelectPhysicalTablesWizardPage extends WizardPage {

	private Table newTables, importedTables;
	private TableItem[] tablesToImport;
	private final PhysicalModel physicalModel;
	private final List<String> missingTablesNames;

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();

	protected SelectPhysicalTablesWizardPage(String pageName, PhysicalModel physicalModel, List<String> missingTablesNames) {
		super(pageName);
		setTitle(RL.getString("physical.editor.wizard.update.selecttables.title"));
		setDescription(RL.getString("physical.editor.wizard.update.selecttables.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.physical.wizards.inline.updateModel"));
		if (image != null)
			setImageDescriptor(image);

		this.physicalModel = physicalModel;
		this.missingTablesNames = missingTablesNames;
	}

	@Override
	public void createControl(Composite parent) {
		// Main composite
		Composite composite = new Composite(parent, SWT.NULL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);

		// Columns Group
		Group columnsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		columnsGroup.setText(RL.getString("physical.editor.wizard.update.selecttables.tables.label"));
		GridLayout glColumns = new GridLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		glColumns.numColumns = 3;
		glColumns.makeColumnsEqualWidth = false;
		columnsGroup.setLayout(glColumns);
		columnsGroup.setLayoutData(gd2);

		// Left table -------------------------------
		Composite compLeft = new Composite(columnsGroup, SWT.NONE);
		GridLayout glL = new GridLayout();
		GridData gdL = new GridData(GridData.FILL_BOTH);
		glL.numColumns = 1;
		compLeft.setLayout(glL);
		compLeft.setLayoutData(gdL);
		Label lblLeftTab = new Label(compLeft, SWT.NONE);
		lblLeftTab.setText(RL.getString("physical.editor.wizard.update.selecttables.newtables.label"));
		newTables = new Table(compLeft, SWT.BORDER | SWT.MULTI);
		newTables.setLayoutData(gdL);

		// Center buttons -------------------------------
		Composite compCenter = new Composite(columnsGroup, SWT.NONE);
		GridLayout glC = new GridLayout();
		glC.numColumns = 1;
		compCenter.setLayout(glC);
		Button bAddAllField = new Button(compCenter, SWT.FLAT);
		bAddAllField.setToolTipText(RL.getString("physical.editor.wizard.update.selecttables.addallbutton"));
		Image imageAddAll = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_right"))
				.createImage();
		if (imageAddAll != null)
			bAddAllField.setImage(imageAddAll);

		Button bAddField = new Button(compCenter, SWT.FLAT);
		bAddField.setToolTipText(RL.getString("physical.editor.wizard.update.selecttables.addbutton"));
		Image imageAdd = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_right")).createImage();
		if (imageAdd != null)
			bAddField.setImage(imageAdd);
		Button bRemoveField = new Button(compCenter, SWT.FLAT);
		bRemoveField.setToolTipText(RL.getString("physical.editor.wizard.update.selecttables.removebutton"));
		Image imageRem = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.arrow_left")).createImage();
		if (imageRem != null)
			bRemoveField.setImage(imageRem);

		Button bRemoveAllField = new Button(compCenter, SWT.FLAT);
		bRemoveAllField.setToolTipText(RL.getString("physical.editor.wizard.update.selecttables.removeallbutton"));
		Image imageRemAll = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.business.wizards.inline.double_arrow_left"))
				.createImage();
		if (imageRemAll != null)
			bRemoveAllField.setImage(imageRemAll);

		// Right table -------------------------------
		Composite compRight = new Composite(columnsGroup, SWT.NONE);
		GridLayout glR = new GridLayout();
		GridData gdR = new GridData(GridData.FILL_BOTH);
		glR.numColumns = 1;
		compRight.setLayout(glR);
		compRight.setLayoutData(gdR);
		Label lblRightTab = new Label(compRight, SWT.NONE);
		lblRightTab.setText(RL.getString("physical.editor.wizard.update.selecttables.importedtables.label"));
		importedTables = new Table(compRight, SWT.BORDER | SWT.MULTI);
		importedTables.setLayoutData(gdR);

		// adding listener to Add button
		bAddField.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				// single selection
				if (newTables.getSelectionCount() == 1)
					tiSel = newTables.getSelection()[0];
				// multi selection
				else if (newTables.getSelectionCount() > 1) {
					int selectionCount = newTables.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i = 0; i < selectionCount; i++) {
						tiMultiSel[i] = newTables.getSelection()[i];
					}
				}
				if (tiSel != null) {
					TableItem ti = new TableItem(importedTables, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());
					newTables.remove(newTables.getSelectionIndex());
				}
				if (tiMultiSel != null) {
					for (int i = 0; i < tiMultiSel.length; i++) {
						TableItem ti = new TableItem(importedTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setData(tiMultiSel[i].getData());
					}
					newTables.remove(newTables.getSelectionIndices());
				}
				// checkPageComplete();
			}
		});

		// adding listener to Remove button
		bRemoveField.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem tiSel = null;
				TableItem[] tiMultiSel = null;
				// single selection
				if (importedTables.getSelectionCount() == 1)
					tiSel = importedTables.getSelection()[0];
				// multi selection
				else if (importedTables.getSelectionCount() > 1) {
					int selectionCount = importedTables.getSelectionCount();
					tiMultiSel = new TableItem[selectionCount];
					for (int i = 0; i < selectionCount; i++) {
						tiMultiSel[i] = importedTables.getSelection()[i];
					}
				}
				if (tiSel != null) {
					TableItem ti = new TableItem(newTables, 0);
					ti.setText(tiSel.getText());
					ti.setData(tiSel.getData());
					importedTables.remove(importedTables.getSelectionIndex());
				}
				if (tiMultiSel != null) {
					for (int i = 0; i < tiMultiSel.length; i++) {
						TableItem ti = new TableItem(newTables, 0);
						ti.setText(tiMultiSel[i].getText());
						ti.setData(tiMultiSel[i].getData());
					}
					importedTables.remove(importedTables.getSelectionIndices());
				}
				// checkPageComplete();

			}
		});

		// adding listener to Add All button
		bAddAllField.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem[] columnToAdd = null;
				columnToAdd = newTables.getItems();

				// add Tables to Imported Tables panel
				for (int i = 0; i < columnToAdd.length; i++) {
					TableItem ti = new TableItem(importedTables, 0);
					ti.setText(columnToAdd[i].getText());
					ti.setData(columnToAdd[i].getData());
				}
				// Remove tables from new Physical Tables panel
				newTables.removeAll();

				// checkPageComplete();
			}
		});

		// adding listener to Remove All button
		bRemoveAllField.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				TableItem[] fieldsToRemove = null;
				fieldsToRemove = importedTables.getItems();

				// add Tales to new Physical Tablse panel
				for (int i = 0; i < fieldsToRemove.length; i++) {
					TableItem ti = new TableItem(newTables, 0);
					ti.setText(fieldsToRemove[i].getText());
					ti.setData(fieldsToRemove[i].getData());
				}
				// Remove tables from Imported Tables panel
				importedTables.removeAll();

				// checkPageComplete();
			}
		});

		// populate tables
		addTableItems();

		// first check
		// checkPageComplete();

		// Important: Setting page control
		setControl(composite);
	}

	// add the new physical tables as TableItem (in the left Table Widget)
	public void addTableItems() {
		newTables.removeAll();
		importedTables.removeAll();

		// Get Columns for a simple Business Tables
		if (missingTablesNames != null) {
			// retrieve Tables Names

			for (String missingTableName : missingTablesNames) {
				TableItem ti = new TableItem(newTables, 0);
				ti.setData(missingTableName);
				ti.setText(missingTableName);
			}
		}

	}

	/**
	 * @return the importedTables
	 */
	public Table getImportedTables() {
		return importedTables;
	}

	/**
	 * @param importedTables
	 *            the importedTables to set
	 */
	public void setImportedTables(Table importedTables) {
		this.importedTables = importedTables;
	}

}
