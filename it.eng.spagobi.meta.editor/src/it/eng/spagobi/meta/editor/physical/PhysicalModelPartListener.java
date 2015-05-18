package it.eng.spagobi.meta.editor.physical;

import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.views.contentoutline.ContentOutline;
import org.eclipse.ui.views.properties.PropertySheet;
import org.slf4j.Logger;

import it.eng.spagobi.meta.editor.business.BusinessModelEditor;
import it.eng.spagobi.meta.editor.commons.DiagnosticPartListener;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 *
 */
public class PhysicalModelPartListener  extends DiagnosticPartListener{
	
	PhysicalModelEditor editor;

	
	public PhysicalModelPartListener(PhysicalModelEditor editor, Logger logger) {
		super(logger);
		this.editor = editor;
	}
	
	public void partActivated(IWorkbenchPart p) {
		logger.trace("IN");
		
		logger.debug("Activated part [{}]", p.getClass().getName());
		if (p instanceof PropertySheet) {
			logger.debug("Activated [{}]", PropertySheet.class.getName());
			if (((PropertySheet)p).getCurrentPage() == editor.getPropertySheetPage()) {
				logger.debug("Activated the property sheet  of this editor");
				editor.getActionBarContributor().setActiveEditor(editor);

			}
		} else if (p == editor) {
			logger.debug("Activated [{}]", PhysicalModelEditor.class.getName());
		}
		
		logger.trace("OUT");
	}
	

}
