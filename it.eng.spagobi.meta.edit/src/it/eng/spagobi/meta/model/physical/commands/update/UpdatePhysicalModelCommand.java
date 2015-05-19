/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.physical.commands.update;

import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.initializer.properties.PhysicalModelPropertiesFromFileInitializer;
import it.eng.spagobi.meta.model.Model;
import it.eng.spagobi.meta.model.ModelProperty;
import it.eng.spagobi.meta.model.business.commands.edit.AbstractSpagoBIModelEditCommand;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class UpdatePhysicalModelCommand extends AbstractSpagoBIModelEditCommand {
	PhysicalModel physicalModel;
	private static Logger logger = LoggerFactory.getLogger(UpdatePhysicalModelCommand.class);

	public UpdatePhysicalModelCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.physical.commands.updatemodel.label", "model.physical.commands.updatemodel.description", "model.physical.commands.updatemodel", domain,
				parameter);
	}

	public UpdatePhysicalModelCommand(EditingDomain domain) {
		this(domain, null);
	}

	@Override
	public void execute() {
		logger.debug("Executing UpdatePhysicalModelCommand");

		physicalModel = (PhysicalModel) parameter.getOwner();

		ProgressMonitorDialog dialog = new ProgressMonitorDialog(new Shell());
		dialog.setCancelable(false);

		try {
			dialog.run(true, false, new IRunnableWithProgress() {
				@Override
				public void run(IProgressMonitor monitor) {
					// Note: this is a non-UI Thread
					monitor.beginTask("Updating Physical Model, please wait...", IProgressMonitor.UNKNOWN);
					// doing task...
					updatePhysicalModel(physicalModel);

					monitor.done();
				}
			});
		} catch (InvocationTargetException e1) {
			e1.printStackTrace();
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		showInformation("Info", "Physical Model Updated ");

	}

	private void updatePhysicalModel(PhysicalModel physicalModel) {
		// Retrieve connection's informations from current physical Model

		String modelName = physicalModel.getName();

		String connectionName = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_NAME);
		logger.debug("PhysicalModel Property: Connection name is [{}]", connectionName);

		String connectionDriver = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_DRIVER);
		logger.debug("PhysicalModel Property: Connection driver is [{}]", connectionDriver);

		String connectionUrl = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_URL);
		logger.debug("PhysicalModel Property: Connection url is [{}]", connectionUrl);

		String connectionUsername = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_USERNAME);
		logger.debug("PhysicalModel Property: Connection username is [{}]", connectionUsername);

		String connectionPassword = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_PASSWORD);
		logger.debug("PhysicalModel Property: Connection password is [{}]", connectionPassword);

		String connectionDatabaseName = getProperty(physicalModel, PhysicalModelPropertiesFromFileInitializer.CONNECTION_DATABASENAME);
		logger.debug("PhysicalModel Property: Connection databasename is [{}]", connectionDatabaseName);

		String catalog = physicalModel.getCatalog();
		logger.debug("PhysicalModel: Connection catalog is [{}]", catalog);

		String schema = physicalModel.getSchema();
		logger.debug("PhysicalModel: Connection schema is [{}]", schema);

		PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
		physicalModelInitializer.setRootModel(physicalModel.getParentModel());

		// Retrieve connection from command parameter (from DSEBridge)
		Connection connection = (Connection) this.getParameter().getValue();
		List<String> tablesToAdd = (List<String>) this.getParameter().getCollection();

		PhysicalModel updatedPhysicalModel = physicalModelInitializer.initialize(modelName + "_updated", connection, connectionName, connectionDriver,
				connectionUrl, connectionUsername, connectionPassword, connectionDatabaseName, catalog, schema);
		Model model = updatedPhysicalModel.getParentModel();

		// remove reference from the new physical model to the generic Model (automatically vice-versa)
		updatedPhysicalModel.setParentModel(null);

		// update original physical tables with new informations from new model
		PhysicalModel originalModelUpdated = physicalModelInitializer.updateModel(physicalModel, updatedPhysicalModel, tablesToAdd);

		this.executed = true;

	}

	// This command can't be undone
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public Collection<?> getAffectedObjects() {
		Collection affectedObjects = Collections.EMPTY_LIST;
		if (physicalModel != null) {
			affectedObjects = new ArrayList();
			affectedObjects.add(physicalModel);
		}
		return affectedObjects;
	}

	private String getProperty(PhysicalModel physicalModel, String propertyName) {
		ModelProperty property = physicalModel.getProperties().get(propertyName);
		return property != null ? property.getValue() : null;
	}

	/**
	 * Show an information dialog box.
	 */
	public void showInformation(final String title, final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				MessageDialog.openInformation(null, title, message);
			}
		});
	}
}
