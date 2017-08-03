package com.sevtekin.am.common.config;

import java.util.ArrayList;
import java.util.List;

public class ConfigReader {
	//public static String configFile = System.getProperty("user.home")
	//		+ "/.am/am.properties";

	public ConfigReader() {
	}

	public String getDBURL() {
		//return new PropertiesParser().getPropertyValue(configFile, "DBURL");
		
		return System.getenv("DBURL");
	}
	
	
	public String getDBHost() {
		String url =  this.getDBURL();
		int pos1= url.indexOf("://");
		int pos2= url.indexOf(":",pos1+3);
		return url.substring(pos1+3, pos2);
	}
	
	public String getDBPort() {
		String url =  this.getDBURL();
		int pos1= url.indexOf("://");
		int pos2= url.indexOf(":",pos1+3);
		int pos3= url.indexOf("/",pos2+1);
		return url.substring(pos2+1, pos3);
	}
	
	public String getDBName() {
		//String url =  new PropertiesParser().getPropertyValue(configFile, "DBURL");
		String url =  this.getDBURL();
		int pos =  url.lastIndexOf("/");
		return url.substring(pos+1, url.length());
	}

	public String getDBUser() {
		//return new PropertiesParser().getPropertyValue(configFile, "DBUSER");
		
		return System.getenv("DBUSER");
	}

	public String getDBPassword() {
		return System.getenv("DBPASSWORD");
	}
	
	public String getDBDumpLocation() {
		//return new PropertiesParser().getPropertyValue(configFile, "DBDUMPLOCATION");
		
		return System.getenv("DBDUMPLOCATION");
	}

	public String getServiceUriRoot() {
		//return new PropertiesParser().getPropertyValue(configFile,"SERVICEURIROOT");
		
		return System.getenv("SERVICEURIROOT");
	}

	public List<String> getAllowedConsumerList() {
		List<String> consumerList = new ArrayList<String>();
		//String strLine = new PropertiesParser().getPropertyValue(configFile,"ALLOWEDCONSUMERS");
		
		String strLine = System.getenv("ALLOWEDCONSUMERS");
		String[] consumers = strLine.split(",");
		for (String consumer : consumers)
			consumerList.add(consumer);
		return consumerList;
	}
}
