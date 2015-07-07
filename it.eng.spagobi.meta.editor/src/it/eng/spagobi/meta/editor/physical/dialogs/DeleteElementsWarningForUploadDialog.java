/**
 * 
 */
package it.eng.spagobi.meta.editor.physical.dialogs;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.physical.PhysicalColumn;
import it.eng.spagobi.meta.model.physical.PhysicalTable;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.net.URL;
import java.util.Collection;

import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ListDialog;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class DeleteElementsWarningForUploadDialog extends ListDialog {

	public DeleteElementsWarningForUploadDialog(Collection<ModelObject> markedElements) {
		super(new Shell());
		this.setContentProvider(new ArrayContentProvider());
		this.setTitle("Warning");
		this.setMessage("You must delete the followings physical objects before proceeding.\nOpen the file in the editor and delete the following elements.");
		this.setHelpAvailable(false);
		this.setLabelProvider(new ArrayLabelProvider());
		this.setAddCancelButton(false);

		this.setInput(markedElements);
	}

	static class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof PhysicalColumn) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/PhysicalColumnDeleted")).createImage();
			} else if (element instanceof PhysicalTable) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/PhysicalTableDeleted")).createImage();
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof ModelObject) {
				ModelObject modelObject = (ModelObject) element;
				return modelObject.getName();
			}
			return null;
		}

	}

	public static ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}
}
