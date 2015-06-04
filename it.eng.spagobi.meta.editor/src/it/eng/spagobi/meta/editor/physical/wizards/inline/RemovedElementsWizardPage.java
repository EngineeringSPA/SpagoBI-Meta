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
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.net.URL;
import java.util.List;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class RemovedElementsWizardPage extends WizardPage {

	private final List<String> removedElementsNames;

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();

	private boolean automaticDelete = false;

	protected RemovedElementsWizardPage(String pageName, List<String> removedElementsNames) {
		super(pageName);
		setTitle(RL.getString("physical.editor.wizard.update.removedelements.title"));
		setDescription(RL.getString("physical.editor.wizard.update.removedelements.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.physical.wizards.inline.updateModel"));
		if (image != null)
			setImageDescriptor(image);

		this.removedElementsNames = removedElementsNames;
	}

	@Override
	public void createControl(Composite parent) {
		// Main composite
		Composite composite = new Composite(parent, SWT.FILL);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.makeColumnsEqualWidth = true;
		composite.setLayout(gl);

		// Columns Group
		Group columnsGroup = new Group(composite, SWT.SHADOW_ETCHED_IN);
		columnsGroup.setText(RL.getString("physical.editor.wizard.update.removedelements.group.label"));
		FillLayout flColumns = new FillLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		columnsGroup.setLayout(flColumns);
		columnsGroup.setLayoutData(gd2);

		TableViewer tableViewer = new TableViewer(columnsGroup);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ArrayLabelProvider());
		tableViewer.setInput(removedElementsNames);

		Button automaticDeleteCheck = new Button(composite, SWT.CHECK);
		automaticDeleteCheck.setText(RL.getString("physical.editor.wizard.update.removedelements.autodelete.label"));

		automaticDeleteCheck.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent event) {
				if (((Button) event.widget).getSelection()) {
					// checked
					automaticDelete = true;
				} else {
					// unchecked
					automaticDelete = false;
				}
			}
		});

		if (removedElementsNames.isEmpty()) {
			// disable checkbox
			automaticDeleteCheck.setEnabled(false);
		}

		// Important: Setting page control
		setControl(composite);
	}

	/**
	 * @return the automaticDelete
	 */
	public synchronized boolean isAutomaticDelete() {
		return automaticDelete;
	}

	static class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof String) {
				String stringElement = (String) element;
				if (stringElement.contains(".")) {
					// it's a column name
					return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/PhysicalColumnDeleted")).createImage();

				} else {
					// it's a table name
					return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/PhysicalTableDeleted")).createImage();

				}
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof String) {
				return (String) element;
			}
			return null;
		}

	}

	public static ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}

}
