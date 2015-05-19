/**
 * 
 */
package it.eng.spagobi.meta.editor.physical.actions;

import it.eng.spagobi.meta.editor.business.actions.AbstractSpagoBIModelAction;
import it.eng.spagobi.meta.editor.multi.DSEBridge;
import it.eng.spagobi.meta.editor.physical.wizards.inline.UpdatePhysicalModelWizard;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.initializer.properties.PhysicalModelPropertiesFromFileInitializer;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.phantom.provider.PhysicalRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.commands.update.UpdatePhysicalModelCommand;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchPart;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class UpdatePhysicalModelAction extends AbstractSpagoBIModelAction {

	private ISpagoBIModelCommand performFinishCommand;

	/**
	 * @param commandClass
	 * @param workbenchPart
	 * @param selection
	 */
	public UpdatePhysicalModelAction(IWorkbenchPart workbenchPart, ISelection selection) {
		super(UpdatePhysicalModelCommand.class, workbenchPart, selection);
		if (command instanceof ISpagoBIModelCommand)
			this.performFinishCommand = (ISpagoBIModelCommand) command;
	}

	/**
	 * This executes the command.
	 */
	@Override
	public void run() {
		try {
			boolean result = MessageDialog.openConfirm(new Shell(), "Physical Model Update", "Do you want to update the physical model (cannot be undone)?");

			if (result) {
				// OK Button selected do something
				PhysicalRootItemProvider owner = (PhysicalRootItemProvider) this.owner;
				PhysicalModel physicalModel = (PhysicalModel) owner.getParentObject();
				String connectionName = physicalModel.getProperties().get(PhysicalModelPropertiesFromFileInitializer.CONNECTION_NAME).getValue();
				// Create connection using DSEBridge - using the same connection name retrieved from the physical model
				DSEBridge dseBridge = new DSEBridge();
				Connection connection = dseBridge.connect(connectionName);

				// Get List of possibles tables names missing in the physical model
				PhysicalModelInitializer physicalModelInitializer = new PhysicalModelInitializer();
				List<String> missingTablesNames = physicalModelInitializer.getMissingTablesNames(connection, physicalModel);

				// Open the Wizard for selecting tables to import only if there are missing tables in the current physical model
				if (!missingTablesNames.isEmpty()) {
					UpdatePhysicalModelWizard wizard = new UpdatePhysicalModelWizard(physicalModel, connection, missingTablesNames, editingDomain,
							(ISpagoBIModelCommand) command);
					WizardDialog dialog = new WizardDialog(new Shell(), wizard);
					dialog.create();
					dialog.open();
				} else {
					// just execute the command (for deleted tables ecc)
					performFinishCommand.setParameter(new CommandParameter(physicalModel, null, connection, new ArrayList<Object>()));
					// this guard is for extra security, but should not be necessary
					if (editingDomain != null && performFinishCommand != null) {
						// use up the command
						editingDomain.getCommandStack().execute(performFinishCommand);
					}
				}

				// Commented beheviour without table filter wizard
				// Retrieve connection Name
				// CommandParameter parameter = performFinishCommand.getParameter();
				//
				//
				// PhysicalRootItemProvider owner = (PhysicalRootItemProvider)parameter.getOwner();
				// PhysicalModel physicalModel = (PhysicalModel) owner.getParentObject();
				// String connectionName = physicalModel.getProperties().get(PhysicalModelPropertiesFromFileInitializer.CONNECTION_NAME).getValue();
				// //Create connection using DSEBridge - using the same connection name retrieved from the physical model
				// DSEBridge dseBridge = new DSEBridge();
				// Connection connection = dseBridge.connect(connectionName);
				// performFinishCommand.setParameter(new CommandParameter(owner, null,connection , new ArrayList<Object>()));
				//
				// // this guard is for extra security, but should not be necessary
				// if (editingDomain != null && performFinishCommand != null) {
				// // use up the command
				// editingDomain.getCommandStack().execute(performFinishCommand);
				// }
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * @return the performFinishCommand
	 */
	public ISpagoBIModelCommand getPerformFinishCommand() {
		return performFinishCommand;
	}

}
