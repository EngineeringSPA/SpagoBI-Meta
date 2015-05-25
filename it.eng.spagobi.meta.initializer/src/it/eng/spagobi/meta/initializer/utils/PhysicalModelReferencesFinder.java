/**
 * 
 */
package it.eng.spagobi.meta.initializer.utils;

import it.eng.spagobi.meta.initializer.ModelSingleton;
import it.eng.spagobi.meta.model.ModelObject;
import it.eng.spagobi.meta.model.business.BusinessColumn;
import it.eng.spagobi.meta.model.business.BusinessColumnSet;
import it.eng.spagobi.meta.model.business.BusinessRelationship;
import it.eng.spagobi.meta.model.business.BusinessTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature.Setting;
import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * This class is used to retrieve objects in the Business Model that are related to object of the Physical Model
 * 
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class PhysicalModelReferencesFinder {
	ModelSingleton modelSingleton;
	ECrossReferenceAdapter crossReferenceAdapter;

	public PhysicalModelReferencesFinder() {
		modelSingleton = ModelSingleton.getInstance();
		crossReferenceAdapter = modelSingleton.getCrossReferenceAdapter();

	}

	public List<ModelObject> getBusinessObjects(ModelObject physicalObject) {
		List<ModelObject> businessObjects = new ArrayList<ModelObject>();
		Collection<Setting> settings = crossReferenceAdapter.getInverseReferences(physicalObject);
		for (Setting setting : settings) {
			EObject eobject = setting.getEObject();
			if ((eobject instanceof BusinessRelationship) || (eobject instanceof BusinessTable) || (eobject instanceof BusinessColumn)
					|| (eobject instanceof BusinessColumnSet)) {
				businessObjects.add((ModelObject) eobject);
			}
		}
		return businessObjects;
	}

}