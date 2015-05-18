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
	IWizardPage pageOne;

	public UpdatePhysicalModelWizard(PhysicalModel physicalModel, Connection connection, List<String> missingTablesNames, EditingDomain editingDomain,
			ISpagoBIModelCommand command) {
		super(editingDomain, command);
		this.setWindowTitle("Physical Model Update");
		this.setHelpAvailable(false);
		this.physicalModel = physicalModel;
		this.connection = connection;
		this.missingTablesNames = missingTablesNames;
	}

	@Override
	public void addPages() {

		pageOne = new SelectPhysicalTablesWizardPage("Physical Model update page one", physicalModel, missingTablesNames);
		addPage(pageOne);
	}

	@Override
	public CommandParameter getCommandInputParameter() {
		SelectPhysicalTablesWizardPage wizardPage = (SelectPhysicalTablesWizardPage) pageOne;

		TableItem[] tablesToImport = wizardPage.getImportedTables().getItems();
		int numCol = tablesToImport.length;
		List<String> tablesList = new ArrayList<String>();
		for (int i = 0; i < numCol; i++) {
			String tableName = tablesToImport[i].getText();
			tablesList.add(tableName);
		}

		return new CommandParameter(physicalModel, null, connection, tablesList);
	}

	@Override
	public boolean isWizardComplete() {
		return getStartingPage().isPageComplete();
	}

}
