/**
 * 
 */
package it.eng.spagobi.meta.initializer;

import it.eng.spagobi.meta.model.Model;

import org.eclipse.emf.ecore.util.ECrossReferenceAdapter;

/**
 * Singleton class for passing object references
 * 
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class ModelSingleton {

	private static ModelSingleton istance;
	private Model model;
	private Object editingDomain;
	private ECrossReferenceAdapter crossReferenceAdapter;

	private ModelSingleton() {
	}

	public static ModelSingleton getInstance() {
		if (istance == null) {
			istance = new ModelSingleton();
		}

		return istance;
	}

	/**
	 * @return the model
	 */
	public Model getModel() {
		return model;
	}

	/**
	 * @param model
	 *            the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
	}

	/**
	 * @return the editingDomain
	 */
	public Object getEditingDomain() {
		return editingDomain;
	}

	/**
	 * @param editingDomain
	 *            the editingDomain to set
	 */
	public void setEditingDomain(Object editingDomain) {
		this.editingDomain = editingDomain;
	}

	/**
	 * @return the crossReferenceAdapter
	 */
	public ECrossReferenceAdapter getCrossReferenceAdapter() {
		return crossReferenceAdapter;
	}

	/**
	 * @param crossReferenceAdapter
	 *            the crossReferenceAdapter to set
	 */
	public void setCrossReferenceAdapter(ECrossReferenceAdapter crossReferenceAdapter) {
		this.crossReferenceAdapter = crossReferenceAdapter;
	}

}
