/**
 * 
 */
package it.eng.spagobi.meta.model.business.commands.generate;

/**
 * @author Marco Cortella (marco.cortella@eng.it)
 * 
 */
public class JPAMappingOptionsDescriptor {

	String modelName;
	String catalogName;
	String schemaName;

	/**
	 * @param modelName
	 * @param catalogName
	 * @param schemaName
	 */
	public JPAMappingOptionsDescriptor(String modelName, String catalogName, String schemaName) {
		super();
		this.modelName = modelName;
		this.catalogName = catalogName;
		this.schemaName = schemaName;
	}

	/**
	 * @return the modelName
	 */
	public String getModelName() {
		return modelName;
	}

	/**
	 * @return the catalogName
	 */
	public String getCatalogName() {
		return catalogName;
	}

	/**
	 * @return the schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * @param modelName
	 *            the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @param catalogName
	 *            the catalogName to set
	 */
	public void setCatalogName(String catalogName) {
		this.catalogName = catalogName;
	}

	/**
	 * @param schemaName
	 *            the schemaName to set
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

}
