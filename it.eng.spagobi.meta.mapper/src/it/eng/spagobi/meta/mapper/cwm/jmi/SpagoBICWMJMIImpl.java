/**
 SpagoBI, the Open Source Business Intelligence suite

 Copyright (C) 2012 Engineering Ingegneria Informatica S.p.A. - SpagoBI Competency Center
 This Source Code Form is subject to the terms of the Mozilla Public
 License, v. 2.0. If a copy of the MPL was not distributed with this file,
 You can obtain one at http://mozilla.org/MPL/2.0/.
 
**/
package it.eng.spagobi.meta.mapper.cwm.jmi;

import it.eng.spagobi.meta.cwm.jmi.spagobi.SpagobiPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.MetaPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.behavioral.BehavioralPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.businessinformation.BusinessInformationPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.core.CorePackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.instance.InstancePackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.keysindexes.KeysIndexesPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.multidimensional.MultidimensionalPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.olap.OlapPackage;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.CwmCatalog;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.CwmTable;
import it.eng.spagobi.meta.cwm.jmi.spagobi.meta.relational.RelationalPackage;
import it.eng.spagobi.meta.mapper.cwm.CWMImplType;
import it.eng.spagobi.meta.mapper.cwm.ICWM;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.jmi.model.ModelPackage;
import javax.jmi.model.MofPackage;
import javax.jmi.reflect.RefPackage;

import org.netbeans.api.mdr.MDRepository;
import org.netbeans.api.xmi.XMIReader;
import org.netbeans.api.xmi.XMIReaderFactory;
import org.netbeans.api.xmi.XMIWriter;
import org.netbeans.api.xmi.XMIWriterFactory;
import org.netbeans.mdr.NBMDRepositoryImpl;

/**
 * @author Andrea Gioia (andrea.gioia@eng.it)
 *
 */
public class SpagoBICWMJMIImpl implements ICWM {
	
	private static MDRepository        	repository;
	
	private String 						name;
    
    private SpagobiPackage             	spagobiPackage; // Top level package
    private MetaPackage                	metaPackage;    // meta package

    private RelationalPackage          	relationalPackage;
    private CorePackage                	corePackage;
    private BusinessInformationPackage 	businessInformationPackage;
    private KeysIndexesPackage         	keysIndexesPackage;
    private MultidimensionalPackage    	multiDimensionalPackage;
    private InstancePackage            	instancePackage;
    private BehavioralPackage          	behavioralPackage;
    private OlapPackage                	olapPackage;
	
    public static final String CWM = "CWM-Model-M3"; //$NON-NLS-1$
    
    public SpagoBICWMJMIImpl(String modelName) {
    	try {
            repository = getRepositoryInstance();
            
            this.name = modelName;
         
            /* 
             * Load the M3 CWM model
             */
            RefPackage cwmPackageM3 = repository.getExtent(CWM);
            if( cwmPackageM3 == null) {
                cwmPackageM3 = repository.createExtent(CWM);
                BufferedInputStream inputStream = new BufferedInputStream(getClass().getResourceAsStream("SpagoBICWM.xml")); //$NON-NLS-1$
                XMIReaderFactory.getDefault().createXMIReader().read(inputStream, null, cwmPackageM3);
            }

            /*
             * Create an extent for the domain if that extent doesn't exist yet.
             */
            RefPackage refPackage = repository.getExtent("spagobi");
            
            try {
            	spagobiPackage = (SpagobiPackage) refPackage;
            } catch (Exception e) {
            	e.printStackTrace();
            }
            
            if( spagobiPackage == null ) {
                spagobiPackage  = (SpagobiPackage) repository.createExtent(name, getModelPackage("Spagobi") );              
            }
                        
            // The rest is just derived...
            
            metaPackage = spagobiPackage.getMeta();
            
            corePackage                = metaPackage.getCore();
            relationalPackage          = metaPackage.getRelational();
            businessInformationPackage = metaPackage.getBusinessInformation();
            keysIndexesPackage         = metaPackage.getKeysIndexes();
            multiDimensionalPackage    = metaPackage.getMultidimensional();
            instancePackage            = metaPackage.getInstance();
            behavioralPackage          = metaPackage.getBehavioral();
            olapPackage                = metaPackage.getOlap();
            
        } catch(Throwable e) {
        	throw new RuntimeException("Cannot initialize repository", e);
        }
    }
    
    
    // -----------------------------------------------------------------------------
	// accessor methods
	// -----------------------------------------------------------------------------
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public CWMImplType getImplementationType() {
		return CWMImplType.JMI;
	}
   
    // -----------------------------------------------------------------------------
	// creation methods
	// -----------------------------------------------------------------------------
    
	public CwmCatalog createCatalog(String name) {
	   CwmCatalog catalog = relationalPackage.getCwmCatalog().createCwmCatalog();
	   catalog.setName(name);
	        
	   return catalog;
	}
	
	public CwmTable createTable(String name) {
		CwmTable table = relationalPackage.getCwmTable().createCwmTable();
	    table.setName(name);
	        
	    return table;
	}
	
	public CwmCatalog getCatalog() {
        Collection<CwmCatalog> catalogs = relationalPackage.getCwmCatalog().refAllOfClass();
        CwmCatalog[] c = (CwmCatalog[])catalogs.toArray(new CwmCatalog[catalogs.size()]);
        return (c.length > 0 ? c[0] : null);
	}
	
	private MofPackage getModelPackage(String packageName) {
        ModelPackage mofPackage = (ModelPackage)repository.getExtent(CWM);
        
        for (Iterator it = mofPackage.getMofPackage().refAllOfClass().iterator(); it.hasNext();) {
            MofPackage result = (MofPackage)it.next();
            if (result.getName().equals(packageName)) {
                return result;
            }
        }
        
        return null;
    }
	
	// -----------------------------------------------------------------------------
	// import/export methods
	// -----------------------------------------------------------------------------
	
	public void exportToXMI(String filename) {
		 XMIWriterFactory factory = XMIWriterFactory.getDefault();
         XMIWriter writer = factory.createXMIWriter();
         writer.getConfiguration().setEncoding("UTF-8");
         try {
        	 writer.write(new FileOutputStream(filename), spagobiPackage, "1.2"); //$NON-NLS-1$
         } catch (Throwable t) {
        	 throw new RuntimeException("Impossible to export cwm model [" + name + "] to xmi", t);
         }
    }
	
	public void importFromXMI(String filename) {
        FileInputStream inputStream;
        try {
        	inputStream = new FileInputStream(filename);
        	importFromXMI( inputStream );
        } catch (Throwable t) {
        	throw new RuntimeException("Impossible to import cwm from xmi", t);
        }
    }
	
	public void importFromXMI( InputStream inputStream ){
		try {
	        XMIReaderFactory factory = XMIReaderFactory.getDefault();
	        XMIReader reader = factory.createXMIReader();
	        reader.read(inputStream, null, spagobiPackage);
	        inputStream.close();
		} catch (Throwable t) {
        	throw new RuntimeException("Impossible to import cwm from xmi", t);
        } 
    }
	
	// -----------------------------------------------------------------------------
	// repository methods 
	// -----------------------------------------------------------------------------
	
	private synchronized static final MDRepository getRepositoryInstance()
    {
        // if (repository != null) return repository;
        repository = getRepository();
        return repository;
    }

    private static final MDRepository getRepository() 
    {
        try
        {
            Properties properties = new Properties();
            properties.load(new FileInputStream("conf/repository.properties")); //$NON-NLS-1$
            return getRepository( properties, null );
            
        } catch(Exception e) {
            throw new RuntimeException("Impossible to instatiate JMI repository", e);
        }
    }

    private static final MDRepository getRepository( Properties properties, InputStream xmiInputStream ) {
        try {
            // The system relies on properties set in the virtual machine (system wide)
            Properties systemProperties = System.getProperties();
            Map<Object,Object> backup = new Properties();
            @SuppressWarnings("all")
            Map<Object,Object> m = (Map<Object,Object>)systemProperties.clone();
            backup.putAll(m);
            
            systemProperties.putAll(properties);
            
            String storageFactoryClassName = System.getProperty("org.netbeans.mdr.storagemodel.StorageFactoryClassName", ""); //$NON-NLS-1$ //$NON-NLS-2$
    
            try {
                MDRepository mdRepository = new NBMDRepositoryImpl();

                RefPackage cwmPackageM3 = mdRepository.getExtent(CWM);
                if (cwmPackageM3==null && xmiInputStream != null ) {
                    cwmPackageM3 = mdRepository.createExtent(CWM);
                    BufferedInputStream inputStream = new BufferedInputStream( xmiInputStream );
                    XMIReaderFactory.getDefault().createXMIReader().read(inputStream, null, cwmPackageM3);
                }

                return mdRepository;
            } catch(Exception e) {
            	throw new RuntimeException("Unable to access class [" + storageFactoryClassName + "]", e);
            }
        } catch(Exception e) {
        	throw new RuntimeException("Cannot access repository", e);
        }
    }
}
