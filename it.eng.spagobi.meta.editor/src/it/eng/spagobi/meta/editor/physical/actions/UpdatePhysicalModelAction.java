/**
 * 
 */
package it.eng.spagobi.meta.editor.physical.actions;

import it.eng.spagobi.meta.editor.business.actions.AbstractSpagoBIModelAction;
import it.eng.spagobi.meta.editor.multi.DSEBridge;
import it.eng.spagobi.meta.editor.physical.wizards.inline.UpdatePhysicalModelWizard;
import it.eng.spagobi.meta.initializer.PhysicalModelInitializer;
import it.eng.spagobi.meta.initializer.properties.PhysicalModelPropertiesFromFileInitializer;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.commands.ISpagoBIModelCommand;
import it.eng.spagobi.meta.model.phantom.provider.PhysicalRootItemProvider;
import it.eng.spagobi.meta.model.physical.PhysicalModel;
import it.eng.spagobi.meta.model.physical.commands.update.UpdatePhysicalModelCommand;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.common.command.Command;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
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

				// Get List of new columns found in the database for the tables already in the physical model
				List<String> missingColumnsNames = physicalModelInitializer.getMissingColumnsNames(connection, physicalModel);

				// Get List of elements not found on the database but present in the physical model (so they are deleted)

				List<String> removedElements = physicalModelInitializer.getRemovedTablesAndColumnsNames(connection, physicalModel);

				// Open the Wizard for selecting tables to import only if there are missing tables in the current physical model
				UpdatePhysicalModelWizard wizard = new UpdatePhysicalModelWizard(physicalModel, connection, missingColumnsNames, missingTablesNames,
						removedElements, editingDomain, (ISpagoBIModelCommand) command);
				WizardDialog dialog = new WizardDialog(new Shell(), wizard);
				dialog.create();
				dialog.setBlockOnOpen(true);
				int returnCode = dialog.open();
				if (returnCode == Dialog.OK) {
					boolean autoDelete = wizard.isAutomaticDeleteSelected();

					if (autoDelete) {
						// execute a command for mass delete of elements marked as deleted
						List<ModelObject> markedElements = physicalModelInitializer.getElementsMarkedAsDeleted(physicalModel);
						DeletePhysicalModelObjectAction deletePhysicalModelObjectAction = new DeletePhysicalModelObjectAction();
						final Command deleteCommand = deletePhysicalModelObjectAction.createCommand(markedElements);
						// this guard is for extra security, but should not be necessary
						if (editingDomain != null && deleteCommand != null) {
							// use up the command

							ProgressMonitorDialog progressDialog = new ProgressMonitorDialog(new Shell());
							progressDialog.setCancelable(false);

							try {
								progressDialog.run(true, false, new IRunnableWithProgress() {
									@Override
									public void run(IProgressMonitor monitor) {
										// Note: this is a non-UI Thread
										monitor.beginTask("Deleting marked elements, please wait...", IProgressMonitor.UNKNOWN);
										// doing task...

										editingDomain.getCommandStack().execute(deleteCommand);

										monitor.done();
									}
								});
							} catch (InvocationTargetException e1) {
								e1.printStackTrace();
							} catch (InterruptedException e1) {
								e1.printStackTrace();
							}
							showInformation("Info", "Deleted marked elements successfully ");
						}

					}
				}

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
