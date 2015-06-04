/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.editor.physical.wizards.inline;

import it.eng.spagobi.meta.editor.business.wizards.AbstractSpagoBIModelWizard;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.TableItem;

public class UpdatePhysicalModelWizard extends AbstractSpagoBIModelWizard {

	PhysicalModel physicalModel;
	Connection connection;
	List<String> missingTablesNames;
	List<String> missingColumnsNames;
	List<String> removedElements;

	IWizardPage pageTablesSelection, pageNewColumns, pageRemovedElements;

	public UpdatePhysicalModelWizard(PhysicalModel physicalModel, Connection connection, List<String> missingColumnsNames, List<String> missingTablesNames,
			List<String> removedElements, EditingDomain editingDomain, ISpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle("Physical Model Update");
		this.setHelpAvailable(false);
		this.physicalModel = physicalModel;
		this.connection = connection;
		this.missingTablesNames = missingTablesNames;
		this.missingColumnsNames = missingColumnsNames;
		this.removedElements = removedElements;

	}

	@Override
	public void addPages() {

		pageNewColumns = new NewColumnsWizardPage("Physical Model update - new columns", missingColumnsNames);

		pageTablesSelection = new SelectPhysicalTablesWizardPage("Physical Model update - tables selection", physicalModel, missingTablesNames);

		pageRemovedElements = new RemovedElementsWizardPage("Physical Model update - removed elements", removedElements);

		addPage(pageNewColumns);
		addPage(pageTablesSelection);
		addPage(pageRemovedElements);

	}

	@Override
	public CommandParameter getCommandInputParameter() {
		SelectPhysicalTablesWizardPage tablesSelectionWizardPage = (SelectPhysicalTablesWizardPage) pageTablesSelection;

		TableItem[] tablesToImport = tablesSelectionWizardPage.getImportedTables().getItems();
		int numCol = tablesToImport.length;
		List<String> tablesList = new ArrayList<String>();
		for (int i = 0; i < numCol; i++) {
			String tableName = tablesToImport[i].getText();
			tablesList.add(tableName);
		}

		return new CommandParameter(physicalModel, null, connection, tablesList);
	}

	public boolean isAutomaticDeleteSelected() {
		RemovedElementsWizardPage removedElementsWizardPage = (RemovedElementsWizardPage) pageRemovedElements;
		boolean automaticDelete = removedElementsWizardPage.isAutomaticDelete();
		return automaticDelete;
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
