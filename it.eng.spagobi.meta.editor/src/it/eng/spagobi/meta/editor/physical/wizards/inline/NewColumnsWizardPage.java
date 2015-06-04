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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

public class NewColumnsWizardPage extends WizardPage {

	private final List<String> missingColumnsNames;

	private static final IResourceLocator RL = SpagoBIMetaEditorPlugin.getInstance().getResourceLocator();

	protected NewColumnsWizardPage(String pageName, List<String> missingColumnsNames) {
		super(pageName);
		setTitle(RL.getString("physical.editor.wizard.update.newcolumns.title"));
		setDescription(RL.getString("physical.editor.wizard.update.newcolumns.description"));
		ImageDescriptor image = ImageDescriptor.createFromURL((URL) RL.getImage("it.eng.spagobi.meta.editor.physical.wizards.inline.updateModel"));
		if (image != null)
			setImageDescriptor(image);

		this.missingColumnsNames = missingColumnsNames;
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
		columnsGroup.setText(RL.getString("physical.editor.wizard.update.newcolumns.tables.label"));
		FillLayout flColumns = new FillLayout();
		GridData gd2 = new GridData(GridData.FILL_BOTH);
		columnsGroup.setLayout(flColumns);
		columnsGroup.setLayoutData(gd2);

		final TableViewer tableViewer = new TableViewer(columnsGroup);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new ArrayLabelProvider());
		tableViewer.setInput(missingColumnsNames);

		// Important: Setting page control
		setControl(composite);
	}

	static class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof String) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/PhysicalColumn")).createImage();
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
