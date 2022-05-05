package org.glsid.utili;

import java.util.Properties;

public final class ConstantesConfigurations 
{
	private Properties properties = null;
	private static ConstantesConfigurations instance = null;
	

	/** Private constructor */
	private ConstantesConfigurations (){
	    this.properties = new Properties();
	    try{
	        properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("constantes.properties"));
	    }catch(Exception ex){
	        ex.printStackTrace();
	    }
	}   

	/** Creates the instance is synchronized to avoid multithreads problems */
	private synchronized static void createInstance () {
	    if (instance == null) { 
	        instance = new ConstantesConfigurations();
	    }
	}

	/** Get the properties instance. Uses singleton pattern */
	public static ConstantesConfigurations getInstance(){
		
    	
	    // Uses singleton pattern to guarantee the creation of only one instance
	
	    if(instance == null) {
	        createInstance();
	    }
	    return instance;
	}

	/** Get a property of the property file */
	public String getProperty(String key){
	    String result = null;
	    if(key !=null && !key.trim().isEmpty()){
	        result = this.properties.getProperty(key);
	    }
	    return result;
	}

	/** Override the clone method to ensure the "unique instance" requeriment of this class */
	public Object clone() throws CloneNotSupportedException {
	    throw new CloneNotSupportedException();
	}}