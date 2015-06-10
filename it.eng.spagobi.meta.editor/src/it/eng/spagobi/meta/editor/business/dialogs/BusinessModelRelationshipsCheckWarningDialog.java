/**
 * 
 */
package it.eng.spagobi.meta.editor.business.dialogs;

import it.eng.spagobi.meta.initializer.utils.Pair;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
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
public class BusinessModelRelationshipsCheckWarningDialog extends ListDialog {

	public BusinessModelRelationshipsCheckWarningDialog(List<Pair<BusinessRelationship, Integer>> pairRelatioshipRequiredNumber) {
		super(new Shell());
		this.setContentProvider(new ArrayContentProvider());
		this.setTitle("Warning");
		this.setMessage("The following business relationship are not correct for the creation of the JPA Mapping.\n"
				+ "You must have at least the same number of source columns in the relationships and columns set as identifiers in the target table.\n"
				+ "Note that if a table doesn't have identifiers, all his columns will be used in an identifier created automatically.\n"
				+ "Press OK to continue anyway or press CANCEL to go back and correct.");
		this.setHelpAvailable(false);
		this.setLabelProvider(new ArrayLabelProvider());

		this.setInput(pairRelatioshipRequiredNumber);
	}

	static class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {
		@Override
		public String getText(Object element) {
			return getColumnText(element, 0);
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			if (element instanceof Pair<?, ?>) {
				return ImageDescriptor.createFromURL((URL) getResourceLocator().getImage("full/obj16/BusinessRelationship")).createImage();
			}
			return null;
		}

		@Override
		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Pair<?, ?>) {
				Pair<BusinessRelationship, Integer> pairValue = (Pair<BusinessRelationship, Integer>) element;
				BusinessRelationship businessRelationship = pairValue.a;
				Integer requiredNumber = pairValue.b;
				return "Relationship " + businessRelationship.getName() + " must have at least " + requiredNumber + " source columns";
			}
			return null;
		}

	}

	public static ResourceLocator getResourceLocator() {
		return SpagoBIMetaModelEditPlugin.INSTANCE;
	}
}
