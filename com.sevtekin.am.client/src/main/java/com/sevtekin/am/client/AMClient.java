package com.sevtekin.am.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.sevtekin.am.common.CashEntries;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntries;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntries;
import com.sevtekin.am.common.KeywordEntry;
import com.sevtekin.am.common.OwnerEntries;
import com.sevtekin.am.common.OwnerEntry;
import com.sevtekin.am.common.SnapshotEntries;
import com.sevtekin.am.common.SnapshotEntry;
import com.sevtekin.am.common.config.ConfigReader;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;

public class AMClient {

	private String serviceUriRoot = "";

	public AMClient() {
		ConfigReader configReader = new ConfigReader();
		serviceUriRoot = configReader.getServiceUriRoot();
		if (serviceUriRoot.contains("https:")) {
			new SSLHandler();
		}
	}

	private void delete(String url) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			webResource.type("text/plain").post(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<CashEntry> getCash(String url) {
		URL restWebService;
		URLConnection yc;
		String data = null;
		List<CashEntry> entries = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entries = deserializeCashEntries(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}
	
	

	public List<CashEntry> getCashEntries() {
		String url = serviceUriRoot + "/cashentries";
		return getCash(url);
	}
	
	public List<CashEntry> getEstimateEntries() {
		String url = serviceUriRoot + "/estimateentries";
		return getCash(url);
	}

	public List<CashEntry> getCashEntries(String filters) {
		String url = serviceUriRoot + "/cashentries/"
				+ filters.replaceAll(" ", "%20");
		return getCash(url);
	}

	public List<CashEntry> getCashEntriesSumByMonth() {
		String url = serviceUriRoot + "/reports/sumbymonth";
		return getCash(url);
	}

	public List<CashEntry> getCashEntriesSumByOwner() {
		String url = serviceUriRoot + "/reports/sumbyowner";
		return getCash(url);
	}
	
	public CashEntry getCashEntriesSumByYear(int year) {
		String url = serviceUriRoot + "/reports/sumbyyear/"+year;
		URL restWebService;
		URLConnection yc;
		String data = null;
		CashEntry entry = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entry = deserializeCashEntry(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entry;
	}

	public List<CashEntry> getCashEntriesSumByMonthByCategory() {
		String url = serviceUriRoot + "/reports/sumbymonthbycategory";
		return getCash(url);
	}
	public List<CashEntry> getTop10ByCategory(int year,String sort) {
		String url = serviceUriRoot + "/reports/top10bycategory/"+year+"/"+sort;
		return getCash(url);
	}

	private void putCash(String url, CashEntry entry) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			webResource.type("application/xml").post(ClientResponse.class,
					entry);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCashEntry(CashEntry entry) {
		String url = serviceUriRoot + "/addcashentry";
		putCash(url, entry);
	}

	public void updateCashEntry(CashEntry entry) {
		String url = serviceUriRoot + "/updatecashentry";
		putCash(url, entry);
	}
	
	public void updateEstimateEntry(CashEntry entry) {
		String url = serviceUriRoot + "/updateestimateentry";
		putCash(url, entry);
	}

	public void deleteCashEntry(int id) {
		String url = serviceUriRoot + "/deletecashentry/" + id;
		delete(url);
	}

	private static List<CashEntry> deserializeCashEntries(String data) {
		CashEntries listOfEntries = null;
		List<CashEntry> entries = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.CashEntries.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			listOfEntries = (CashEntries) unmarshaller
					.unmarshal(new StringReader(data));
			entries = listOfEntries.getCashEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entries;
	}
	
	private static CashEntry deserializeCashEntry(String data) {
		CashEntry entry = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.CashEntry.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			entry = (CashEntry) unmarshaller.unmarshal(new StringReader(
					data));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entry;
	}

	// CATEGORY

	private List<CategoryEntry> getCategory(String url) {
		URL restWebService;
		URLConnection yc;
		String data = null;
		List<CategoryEntry> entries = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entries = deserializeCategoryEntries(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public List<CategoryEntry> getCategoryEntries() {
		String url = serviceUriRoot + "/categoryentries";
		return getCategory(url);
	}

	public CategoryEntry getCategoryEntry(int id) {
		String url = serviceUriRoot + "/categoryentry/" + id;
		URL restWebService;
		URLConnection yc;
		String data = null;
		CategoryEntry entry = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entry = deserializeCategoryEntry(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entry;
	}

	private void putCategory(String url, CategoryEntry entry) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.type("application/xml").post(
					ClientResponse.class, entry);
			response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addCategoryEntry(CategoryEntry entry) {
		String url = serviceUriRoot + "/addcategoryentry";
		putCategory(url, entry);
	}

	public void updateCategoryEntry(CategoryEntry entry) {
		String url = serviceUriRoot + "/updatecategoryentry";
		putCategory(url, entry);
	}

	public void deleteCategoryEntry(int id) {
		String url = serviceUriRoot + "/deletecategoryentry/" + id;
		delete(url);
	}

	private static List<CategoryEntry> deserializeCategoryEntries(String data) {
		CategoryEntries listOfEntries = null;
		List<CategoryEntry> entries = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.CategoryEntries.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			listOfEntries = (CategoryEntries) unmarshaller
					.unmarshal(new StringReader(data));
			entries = listOfEntries.getCategoryEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entries;
	}

	private static CategoryEntry deserializeCategoryEntry(String data) {
		CategoryEntry entry = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.CategoryEntry.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			entry = (CategoryEntry) unmarshaller.unmarshal(new StringReader(
					data));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entry;
	}

	// OWNER

	private List<OwnerEntry> getOwner(String url) {
		URL restWebService;
		URLConnection yc;
		String data = null;
		List<OwnerEntry> entries = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entries = deserializeOwnerEntries(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public List<OwnerEntry> getOwnerEntries() {
		String url = serviceUriRoot + "/ownerentries";
		return getOwner(url);
	}

	public void putOwner(String url, OwnerEntry entry) {
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.type("application/xml").post(
					ClientResponse.class, entry);
			response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void addOwnerEntry(OwnerEntry entry) {
		String url = serviceUriRoot + "/addownerentry";
		putOwner(url, entry);
	}

	public void updateOwnerEntry(OwnerEntry entry) {
		String url = serviceUriRoot + "/updateownerentry";
		putOwner(url, entry);
	}

	public void deleteOwnerEntry(int id) {
		String url = serviceUriRoot + "/deleteownerentry/" + id;
		delete(url);
	}

	private static List<OwnerEntry> deserializeOwnerEntries(String data) {
		OwnerEntries listOfEntries = null;
		List<OwnerEntry> entries = null;
		// System.out.println(data);
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.OwnerEntries.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			listOfEntries = (OwnerEntries) unmarshaller
					.unmarshal(new StringReader(data));
			entries = listOfEntries.getOwnerEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entries;
	}

	// Keyword

	public List<KeywordEntry> getKeyword(String url) {
		URL restWebService;
		URLConnection yc;
		String data = null;
		List<KeywordEntry> entries = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entries = deserializeKeywordEntries(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}

	public List<KeywordEntry> getKeywordEntries() {
		String url = serviceUriRoot + "/keywordentries";
		return getKeyword(url);
	}

	public String putKeyword(String url, KeywordEntry entry) {
		String ret = "";
		try {
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			ClientResponse response = webResource.type("application/xml").post(
					ClientResponse.class, entry);
			ret=response.getEntity(String.class);
			response.getStatus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public String addKeywordEntry(KeywordEntry entry) {
		String url = serviceUriRoot + "/addkeywordentry";
		return putKeyword(url, entry);
	}

	public void updateKeywordEntry(KeywordEntry entry) {
		String url = serviceUriRoot + "/updateownerentry";
		putKeyword(url, entry);
	}

	public void deleteKeywordEntry(int id) {
		String url = serviceUriRoot + "/deletekeywordentry/" + id;
		delete(url);
	}

	private static List<KeywordEntry> deserializeKeywordEntries(String data) {
		KeywordEntries listOfEntries = null;
		List<KeywordEntry> entries = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.KeywordEntries.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			listOfEntries = (KeywordEntries) unmarshaller
					.unmarshal(new StringReader(data));
			entries = listOfEntries.getKeywordEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entries;
	}

	// BACKUP AND RESTORE

	public void takeSnapshot(String type) {
		try {
			String url = serviceUriRoot + "/takesnapshot/" + type;
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			webResource.type("text/plain").post(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void restoreSnapshot(String snapshot) {
		try {
			String url = serviceUriRoot + "/restoresnapshot/" + snapshot;
			ClientConfig config = new DefaultClientConfig();
			Client client = Client.create(config);
			WebResource webResource = client.resource(url);
			webResource.type("text/plain").post(String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public List<SnapshotEntry> getLastSnapshotDate() {
		String url = serviceUriRoot + "/lastsnapshotdate";
		return getSnapshot(url);
	}
	
	public List<SnapshotEntry> getSnapshotEntries() {
		String url = serviceUriRoot + "/snapshotentries";
		return getSnapshot(url);
	}
	
	public List<SnapshotEntry> getSnapshot(String url) {
		URL restWebService;
		URLConnection yc;
		String data = null;
		List<SnapshotEntry> entries = null;
		try {
			restWebService = new URL(url);
			yc = restWebService.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				data = inputLine;
			}
			in.close();
			entries = deserializeSnapshotEntries(data);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return entries;
	}
	
	public void deleteSnapshotEntry(String snapshot) {
		String url = serviceUriRoot + "/deletesnapshotentry/" + snapshot;
		delete(url);
	}
	
	private static List<SnapshotEntry> deserializeSnapshotEntries(String data) {
		SnapshotEntries listOfEntries = null;
		List<SnapshotEntry> entries = null;
		try {
			JAXBContext jc = JAXBContext
					.newInstance(new Class[] { com.sevtekin.am.common.SnapshotEntries.class });
			Unmarshaller unmarshaller = jc.createUnmarshaller();
			listOfEntries = (SnapshotEntries) unmarshaller
					.unmarshal(new StringReader(data));
			entries = listOfEntries.getSnapshotEntry();
		} catch (JAXBException e) {
			e.printStackTrace();
		}
		return entries;
	}
}
