package com.sevtekin.am.common.config;

import java.util.ArrayList;
import java.util.List;

import com.sevtekin.am.common.utils.PropertiesParser;

public class ConfigReader {
	public static String configFile = System.getProperty("user.home")
			+ "/.am/am.properties";

	public ConfigReader() {
	}

	public String getDBURL() {
		return new PropertiesParser().getPropertyValue(configFile, "DBURL");
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
		String url =  new PropertiesParser().getPropertyValue(configFile, "DBURL");
		int pos =  url.lastIndexOf("/");
		return url.substring(pos+1, url.length());
	}

	public String getDBUser() {
		return new PropertiesParser().getPropertyValue(configFile, "DBUSER");
	}

	public String getDBPassword() {
		String ret = "";
		try {
			ret = new EncryptionHandler().decrypt(new PropertiesParser()
					.getPropertyValue(configFile, "DBPASSWORD"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
	
	public String getDBDumpLocation() {
		return new PropertiesParser().getPropertyValue(configFile, "DBDUMPLOCATION");
	}

	public String getServiceUriRoot() {
		return new PropertiesParser().getPropertyValue(configFile,
				"SERVICEURIROOT");
	}

	public List<String> getAllowedConsumerList() {
		List<String> consumerList = new ArrayList<String>();
		String strLine = new PropertiesParser().getPropertyValue(configFile,
				"ALLOWEDCONSUMERS");
		String[] consumers = strLine.split(",");
		for (String consumer : consumers)
			consumerList.add(consumer);
		return consumerList;
	}
}
