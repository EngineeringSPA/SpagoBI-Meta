/**
 * 
 */
package it.eng.spagobi.meta.editor.physical.dialogs;

import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;
import it.eng.spagobi.meta.model.business.BusinessView;
import it.eng.spagobi.meta.model.provider.SpagoBIMetaModelEditPlugin;

import java.net.URL;
import java.util.List;

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
public class PhysicalObjectDeleteDialog extends ListDialog {

	public PhysicalObjectDeleteDialog(List<ModelObject> businessObjectToDelete) {
		// ListDialog dialog = new ListDialog(new Shell());
		super(new Shell());
		this.setContentProvider(new ArrayContentProvider());
		this.setTitle("Delete Physical Object");
		this.setMessage("Also the following items on the Business Model will be deleted. Delete this Physical Object?");

		this.setLabelProvider(new ArrayLabelProvider());

		/*
		 * List<String> input = new ArrayList<String>(); input.add("Tom"); input.add("Jack"); input.add("Leo"); input.add("Tom"); input.add("Jack");
		 * input.add("Leo"); input.add("Tom"); input.add("Jack"); input.add("Leo"); input.add("Tom"); input.add("Jack"); input.add("Leo"); input.add("Tom");
		 * input.add("Jack"); input.add("Leo"); input.add("Tom"); input.add("Jack"); input.add("Leo"); input.add("Tom"); input.add("Jack"); input.add("Leo");
		 * input.add("Tom"); input.add("Jack"); input.add("Leo"); input.add("Tom"); input.add("Jack"); input.add("Leo");
		 */
		this.setInput(businessObjectToDelete);
	}

	static class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof BusinessTable) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/BusinessTable")).createImage();
			} else if (element instanceof BusinessColumn) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/BusinessColumn")).createImage();
			} else if (element instanceof BusinessRelationship) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/BusinessRelationship")).createImage();
			} else if (element instanceof BusinessView) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/BusinessView")).createImage();
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			ModelObject modelObject = (ModelObject) element;
			if (modelObject instanceof BusinessColumn) {
				BusinessColumn businessColumn = (BusinessColumn) modelObject;
				return businessColumn.getTable().getName() + "." + businessColumn.getName();
			}
			return modelObject.getName();
		}

	}

	public static ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}
}
