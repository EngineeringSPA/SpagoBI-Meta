/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
 **/
package it.eng.spagobi.meta.model.business.commands.generate;

import it.eng.spagobi.meta.generator.GeneratorDescriptor;
import it.eng.spagobi.meta.generator.GeneratorFactory;
import it.eng.spagobi.meta.generator.jpamapping.JpaMappingJarGenerator;
import it.eng.spagobi.meta.model.business.BusinessModel;
import it.eng.spagobi.meta.model.business.commands.edit.table.ModifyBusinessTableColumnsCommand;
import it.eng.spagobi.meta.model.physical.PhysicalModel;

import java.io.File;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 * 
 */
public class GenerateJPAMappingCommand extends AbstractSpagoBIModelGenerateCommand {

	private static Logger logger = LoggerFactory.getLogger(GenerateJPAMappingCommand.class);

	public GenerateJPAMappingCommand(EditingDomain domain, CommandParameter parameter) {
		super("model.business.commands.generatemapping.label", "model.business.commands.generatemapping.description",
				"model.business.commands.generatemapping", domain, parameter);
	}

	public GenerateJPAMappingCommand(EditingDomain domain) {
		this(domain, null);
	}

	// This command can't be undone
	@Override
	public boolean canUndo() {
		return false;
	}

	@Override
	public void execute() {

		BusinessModel businessModel;
		businessModel = (BusinessModel) parameter.getOwner();
		String directory = (String) parameter.getValue();

		JPAMappingOptionsDescriptor optionsDescriptor = (JPAMappingOptionsDescriptor) parameter.getFeature();
		String schemaName = optionsDescriptor.getSchemaName();
		String catalogName = optionsDescriptor.getCatalogName();
		String modelName = optionsDescriptor.getModelName();

		boolean isUpdatable = optionsDescriptor.isUpdatable();

		String originalSchemaName = getSchemaName(businessModel);
		String originalCatalogName = getCatalogName(businessModel);
		String originalModelName = getModelName(businessModel);

		// set specified model name
		setModelName(businessModel, modelName);

		// set specified schema name for generation
		setSchemaName(businessModel, schemaName);

		// set specified catalog name for generation
		setCatalogName(businessModel, catalogName);

		// Call JPA Mapping generator
		executed = true;
		GeneratorDescriptor descriptor = GeneratorFactory.getGeneratorDescriptorById("it.eng.spagobi.meta.generator.jpamapping");
		JpaMappingJarGenerator generator = null;
		try {
			generator = (JpaMappingJarGenerator) descriptor.getGenerator();
			generator.setLibDir(new File("plugins"));
			generator.generate(businessModel, directory, isUpdatable);
			showInformation("Success", "JPA Mapping generated.\nYou can find the Datamart in the specified path under the \"dist\" directory");
		} catch (Exception e) {
			logger.error("An error occurred while executing command [{}]:", ModifyBusinessTableColumnsCommand.class.getName(), e);
			showInformation("Error in JPAMappingGenerator", "Cannot create JPA Mapping classes");
			executed = false;
		} finally {
			// finally block to hide technical folders created during generation
			if (generator != null) {
				logger.debug("hide techical folders");
				generator.hideTechnicalResources();
			}

			// restore model name to original name
			setModelName(businessModel, originalModelName);

			// restore schema name to original name
			setSchemaName(businessModel, originalSchemaName);

			// restore catalog name to original name
			setCatalogName(businessModel, originalCatalogName);

		}

		if (executed) {
			// showInformation("Successfull Compilation", "JPA Source Code correctly compiled");
			logger.debug("Command [{}] executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
		} else {
			showInformation("Failed Compilation", "Error: JPA Source Code NOT correctly compiled");
			logger.debug("Command [{}] not executed succesfully", ModifyBusinessTableColumnsCommand.class.getName());
		}

	}

	private void setSchemaName(BusinessModel businessModel, String schemaName) {
		if ((schemaName != null) && (schemaName.length() > 0)) {
			PhysicalModel physicalModel = businessModel.getPhysicalModel();
			physicalModel.setSchema(schemaName);
		}

	}

	private String getSchemaName(BusinessModel businessModel) {
		PhysicalModel physicalModel = businessModel.getPhysicalModel();
		String schemaName = physicalModel.getSchema();
		return schemaName;
	}

	private String getCatalogName(BusinessModel businessModel) {
		PhysicalModel physicalModel = businessModel.getPhysicalModel();
		String catalogName = physicalModel.getCatalog();
		return catalogName;
	}

	private void setCatalogName(BusinessModel businessModel, String catalogName) {
		if ((catalogName != null) && (catalogName.length() > 0)) {
			PhysicalModel physicalModel = businessModel.getPhysicalModel();
			physicalModel.setCatalog(catalogName);
		}
	}

	private String getModelName(BusinessModel businessModel) {
		String modelName = businessModel.getName();
		return modelName;
	}

	private void setModelName(BusinessModel businessModel, String modelName) {
		if ((modelName != null) && (modelName.length() > 0)) {
			businessModel.setName(modelName);
		}
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
