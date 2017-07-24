package com.sevtekin.am.common.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PropertiesParser {

	public String getPropertyValue(String filePath, String property) {
		try {
			Properties prop = new Properties();
			prop.load(new FileInputStream(filePath));
			return prop.getProperty(property);
		}
		catch(IOException e) {
			System.out.println("/t/t/tCOULD NOT FIND FILE "+filePath);
			return "";
		}
	}
	
	public void setPropertyValue(String filePath, String property, String value) {
		try {
			 	FileInputStream in = new FileInputStream(filePath);
		        Properties props = new Properties();
		        props.load(in);   
		        in.close();

		        FileOutputStream out = new FileOutputStream(filePath);
		        props.setProperty(property, value);
		        props.store(out, null);
		        out.close();
		}
		catch(IOException e) {
			System.out.println("/t/t/tCOULD NOT FIND FILE "+filePath);
			
		}
	}
	
	public boolean hasProperty(String filePath, String property) throws IOException  {
		Properties prop = new Properties();
		prop.load(new FileInputStream(filePath));
		return prop.containsKey(property);
	}
	
	public List<String> getMultiplePropertiesValue(String filePath, String pattern) throws IOException  {
		List<String> matches = new ArrayList<String>();
		Properties prop = new Properties();
		prop.load(new FileInputStream(filePath));
        Enumeration<?> properties = prop.propertyNames();
        while(properties.hasMoreElements()) {
        	String element = (String) properties.nextElement();
        	if(element.contains(pattern))
        		matches.add(prop.getProperty(element));
        }
        return matches;
	}
	
	public boolean fileContains(String filePath,String value) throws IOException  {
		List<String> lines = new ArrayList<String>();
		lines.clear();
		lines = searchString(filePath, value);
		return !lines.isEmpty();
	}
	
	public List<String> getMultipleValues(String filePath,String key) throws IOException  {
		List<String> values = new ArrayList<String>();
		List<String> lines = cleanLinesNotStartingBy(searchString(filePath, key),key);
		if(!lines.isEmpty()) {
			for(String line: lines) {
				String value = line.replaceAll(key, "").trim();
				if(!values.contains(value))
					values.add(value);
			}
		}
		else
			System.out.println("\t\t\tERROR: PROPERTY "+key+" NOT FOUND");
		return values;
	}
	
	public String getSingleValue(String filePath,String key) throws IOException  {
		List<String> lines = cleanLinesNotStartingBy(searchString(filePath, key),key);
		String value = "";
		if(!lines.isEmpty()) {
			if(lines.size()==1)
				value = lines.get(0).replaceAll(key, "").trim();
			else
				System.out.println("\t\t\tERROR: MULTIPLE VALUES FOUND WHEN SINGLE VALUE WAS EXPECTED");
		}
		else
			System.out.println("\t\t\tERROR: PROPERTY "+key+" NOT FOUND");
		return value;
	}
	
	private List<String> cleanLinesNotStartingBy(List<String> lines, String key) {
		List<String> tempLines = new ArrayList<String>(lines);
		for(String line: lines) {
			if(!line.trim().startsWith(key))
				tempLines.remove(line);
		}
		return tempLines;
			
	}
	
	private List<String> searchString(String fileName, String phrase) throws IOException {
		Scanner fileScanner = new Scanner(new File(fileName));
		List<String> lines = new ArrayList<String>();
		Pattern pattern = Pattern.compile(phrase);
		Matcher matcher = null;
		while (fileScanner.hasNextLine()) {
			String line = fileScanner.nextLine();
			matcher = pattern.matcher(line);
			if ((matcher.find() && (!line.startsWith("#"))))
				lines.add(line);
		}
		fileScanner.close();
		return lines;
	}
}