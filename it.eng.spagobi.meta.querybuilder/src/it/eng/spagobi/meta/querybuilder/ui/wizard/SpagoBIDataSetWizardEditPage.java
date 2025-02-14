/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.querybuilder.ui.wizard;

import it.eng.qbe.query.Query;
import it.eng.qbe.query.serializer.SerializerFactory;
import it.eng.qbe.serializer.SerializationException;
import it.eng.spagobi.meta.querybuilder.ui.QueryBuilder;
import it.eng.spagobi.utilities.exceptions.SpagoBIRuntimeException;

import java.util.Locale;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.IResultSetMetaData;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSetParameters;
import org.eclipse.datatools.connectivity.oda.design.DesignFactory;
import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;
import org.eclipse.datatools.connectivity.oda.design.ResultSetColumns;
import org.eclipse.datatools.connectivity.oda.design.ResultSetDefinition;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;
import org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author cortella
 *
 */
public class SpagoBIDataSetWizardEditPage extends DataSetWizardPage {

    private static String DEFAULT_MESSAGE = "Define the query text for the data set";
    private static Logger logger = LoggerFactory.getLogger(SpagoBIDataSetWizardEditPage.class);
    private String queryText;
    
	public SpagoBIDataSetWizardEditPage( String pageName )
	{
        super( pageName );
        setTitle( pageName );
        setMessage( DEFAULT_MESSAGE );
        
        //this.queryBuilderUI = new QueryBuilder();

	}
	
	public SpagoBIDataSetWizardEditPage( String pageName, QueryBuilder queryBuilderUI )
	{
        super( pageName );
        setTitle( pageName );
        setMessage( DEFAULT_MESSAGE );
        
        //this.queryBuilderUI = queryBuilderUI;
	}
	
	public SpagoBIDataSetWizardEditPage( String pageName, String title,
			ImageDescriptor titleImage )
	{
        super( pageName, title, titleImage );
        setMessage( DEFAULT_MESSAGE );
      //  this.queryBuilderUI = new QueryBuilder();

	}
	
	@Override
	public void createPageCustomControl(Composite parent) {
		initializeControl();
		setControl( createPageControl( parent ) );
		//getQueryBuilder().refreshQueryEditGroup();
	}
	
	//updates the result set page before click the next button
	public IWizardPage getNextPage(){
		getQueryBuilder().refreshQueryResultPage();//update the result table
		return super.getNextPage();
	} 
	
	public QueryBuilder getQueryBuilder() {
		return getSpagoBIWizard().getQueryBuilder();
	}
	
	public SpagoBIDataSetWizard getSpagoBIWizard() {
		return (SpagoBIDataSetWizard)getOdaWizard();
	}
	
    /**
     * Creates custom control for query editing.
     */
    private Control createPageControl( Composite parent ) {
    	Composite composite = getQueryBuilder().createEditComponents(parent);
        setPageComplete( true );
        return composite;
    }
    
	/**
	 * Initializes the page control with the last edited data set design.
	 */
	private void initializeControl( )
	{
		
		queryText =  getInitializationDesign().getQueryText();
 		getSpagoBIWizard().initQueryBuilder(getInitializationDesign());
        validateData();
        setMessage( DEFAULT_MESSAGE );


        /*
         * To optionally honor the request for an editable or
         * read-only design session, use
         *      isSessionEditable();
         */
	}

    /**
     * Obtains the user-defined query text of this data set from page control.
     * @return query text
     */
    private String getQueryText( )  {
    	String serializedQuery = null;
    
    	if(queryText != null){
    	
			try {
				Query query = getQueryBuilder().getQuery();
				query.setId("1232");
				query.setDescription("asd");
				query.setDistinctClauseEnabled(false);
				JSONObject queryJSON =  (JSONObject)SerializerFactory.getSerializer("application/json").serialize(query, getQueryBuilder().getDataSource(), Locale.ENGLISH);
				logger.debug(queryJSON.toString());	
				serializedQuery = queryJSON.toString();
			} catch (SerializationException e) {
				throw new RuntimeException("Impossible to save query", e);
			}
    	}
        return serializedQuery;
    }

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectDataSetDesign(org.eclipse.datatools.connectivity.oda.design.DataSetDesign)
	 */
	protected DataSetDesign collectDataSetDesign( DataSetDesign design )
	{
        if( getControl() == null )     // page control was never created
            return design;             // no editing was done
        if( ! hasValidData() )
            return null;    // to trigger a design session error status
        savePage( design );
        return design;
	}

    /*
     * (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#collectResponseState()
     */
	protected void collectResponseState( )
	{
		super.collectResponseState( );
		/*
		 * To optionally assign a custom response state, for inclusion in the ODA
		 * design session response, use 
         *      setResponseSessionStatus( SessionStatus status );
         *      setResponseDesignerState( DesignerState customState );
		 */
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.oda.design.ui.wizards.DataSetWizardPage#canLeave()
	 */
	protected boolean canLeave( )
	{
        return isPageComplete();
	}

    
	private void validateData( )
	{
        //TODO validate query here, please
		boolean isValid = true;
        if( isValid )
            setMessage( DEFAULT_MESSAGE );
        else
            setMessage( "Requires input value.", ERROR );

		setPageComplete( isValid );
	}

	/**
	 * Indicates whether the custom page has valid data to proceed 
     * with defining a data set.
	 */
	private boolean hasValidData( )
	{
        validateData( );
        
		return canLeave();
	}

	/**
     * Saves the user-defined value in this page, and updates the specified 
     * dataSetDesign with the latest design definition.
	 */
	private void savePage( DataSetDesign dataSetDesign )
	{
        // save user-defined query text
        String queryText = getQueryText();
        dataSetDesign.setQueryText( queryText );

        // obtain query's current runtime metadata, and maps it to the dataSetDesign
        IConnection customConn = null;
        try
        {
            // instantiate your custom ODA runtime driver class
            /* Note: You may need to manually update your ODA runtime extension's
             * plug-in manifest to export its package for visibility here.
             */
            IDriver customDriver = new it.eng.spagobi.meta.querybuilder.oda.Driver();
            
            // obtain and open a live connection
            customConn = customDriver.getConnection( null );
            java.util.Properties connProps =  DesignSessionUtil.getEffectiveDataSourceProperties( getInitializationDesign().getDataSourceDesign() );
            //ad the datasource at the properties..
            connProps.put("data_source", getQueryBuilder().getDataSource());
            customConn.open( connProps );
            
            

            // update the data set design with the 
            // query's current runtime metadata
            updateDesign( dataSetDesign, customConn, queryText );
        }
        catch( OdaException e )
        {
            // not able to get current metadata, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            dataSetDesign.setParameters( null );
            
            e.printStackTrace();
        }
        finally
        {
            closeConnection( customConn );
        }
	}

    /**
     * Updates the given dataSetDesign with the queryText and its derived metadata
     * obtained from the ODA runtime connection.
     */
    private void updateDesign( DataSetDesign dataSetDesign, IConnection conn, String queryText ) throws OdaException
    {
        IQuery query = conn.newQuery( null );
        query.prepare( queryText );
        
        
        
        try {
            IResultSetMetaData md = query.getMetaData();
            updateResultSetDesign( md, dataSetDesign );
        } catch( OdaException e ) {
            // no result set definition available, reset previous derived metadata
            dataSetDesign.setResultSets( null );
            e.printStackTrace();
        }
        
        // proceed to get parameter design definition
        try {
            IParameterMetaData paramMd = query.getParameterMetaData();
            updateParameterDesign( paramMd, dataSetDesign );
        } catch( OdaException ex ) {
            // no parameter definition available, reset previous derived metadata
            dataSetDesign.setParameters( null );
            ex.printStackTrace();
        }
        
        /*
         * See DesignSessionUtil for more convenience methods
         * to define a data set design instance.  
         */     
    }

    /**
     * Updates the specified data set design's result set definition based on the
     * specified runtime metadata.
     * @param md    runtime result set metadata instance
     * @param dataSetDesign     data set design instance to update
     * @throws OdaException
     */
	private void updateResultSetDesign( IResultSetMetaData md,
            DataSetDesign dataSetDesign ) 
        throws OdaException
	{
        ResultSetColumns columns = DesignSessionUtil.toResultSetColumnsDesign( md );

        ResultSetDefinition resultSetDefn = DesignFactory.eINSTANCE
                .createResultSetDefinition();
        // resultSetDefn.setName( value );  // result set name
        resultSetDefn.setResultSetColumns( columns );

        // no exception in conversion; go ahead and assign to specified dataSetDesign
        dataSetDesign.setPrimaryResultSet( resultSetDefn );
        dataSetDesign.getResultSets().setDerivedMetaData( true );
	}

    /**
     * Updates the specified data set design's parameter definition based on the
     * specified runtime metadata.
     * @param paramMd   runtime parameter metadata instance
     * @param dataSetDesign     data set design instance to update
     * @throws OdaException
     */
    private void updateParameterDesign( IParameterMetaData paramMd,
            DataSetDesign dataSetDesign ) 
        throws OdaException
    {
        DataSetParameters paramDesign = 
            DesignSessionUtil.toDataSetParametersDesign( paramMd, 
                    DesignSessionUtil.toParameterModeDesign( IParameterMetaData.parameterModeIn ) );
        
        // no exception in conversion; go ahead and assign to specified dataSetDesign
        dataSetDesign.setParameters( paramDesign );        
        if( paramDesign == null )
            return;     // no parameter definitions; done with update
        
        paramDesign.setDerivedMetaData( true );

        // TODO replace below with data source specific implementation;
        // hard-coded parameter's default value for demo purpose
        if( paramDesign.getParameterDefinitions().size() > 0 )
        {
            ParameterDefinition paramDef = 
                (ParameterDefinition) paramDesign.getParameterDefinitions().get( 0 );
            if( paramDef != null )
                paramDef.setDefaultScalarValue( "dummy default value" );
        }
    }

    /**
     * Attempts to close given ODA connection.
     */
    private void closeConnection( IConnection conn ){
    	try {
        	if( conn != null && conn.isOpen() )
        		conn.close();
		} catch (OdaException e){
            logger.error("Error closing the connection [{}]",conn);
        	throw new SpagoBIRuntimeException("Error closing the connection", e);
		}

    }
}
