package com.sevtekin.am.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntry;
import com.sevtekin.am.common.OwnerEntry;
import com.sevtekin.am.common.SnapshotEntry;
import com.sevtekin.am.common.config.ConfigReader;

public class AMClient {

	public AMClient() {

	}

	static String serviceUriRoot = new ConfigReader().getServiceUriRoot();

	public static List<CashEntry> getCashEntries() {

		// System.out.println("SERVICE URI ROOT " + serviceUriRoot);

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/cashentries");
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			// System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("Enties returned " + entries.size());
		return entries;
	}

	public static List<CashEntry> getCashEntries(String filters) throws IOException {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			// if (filters.contains("description%20like%20%27%25%25%27%20"))
			// filters = filters.substring(36, filters.length());
			//System.out.println(filters);

			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/cashentries/" + filters);
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			// System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
				//System.out.println(entry.getDescription());
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			
			EntityUtils.consume(entity);
			content.close();
			response.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// System.out.println("number of entries returned " + entries.size());
		return entries;

	}

	public static CashEntry getCashEntry(int id) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/cashentry/" + id);
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0);

	}

	public static CategoryEntry getCategoryEntry(int id) {

		CloseableHttpClient client;
		List<CategoryEntry> entries = new ArrayList<CategoryEntry>();
		List<KeywordEntry> keywords = getKeywordEntries();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/categoryentry/" + id);
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CategoryEntry entry = new CategoryEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CategoryEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (CategoryEntry c : entries) {
			List<KeywordEntry> ks = new ArrayList();
			for (KeywordEntry k : keywords)
				if (k.getCategoryId() == c.getId())
					ks.add(k);
			c.setKeywordEntry(ks);
		}
		return entries.get(0);

	}

	public static void addCashEntry(CashEntry entry) {

		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/addcashentry");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();

			// System.out.println(gson.toJson(entry).toString());
			//StringEntity entity = new StringEntity(gson.toJson(entry).toString());
           
			JSONObject obj = new JSONObject();
	        obj.put("description", entry.getDescription());
	        obj.put("amount", entry.getAmount());
	        obj.put("categoryid", entry.getCategoryEntry().getId());
	        obj.put("ownerid", entry.getOwnerEntry().getId());
	        //System.out.println(entry.getActualdate());
	        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(entry.getActualdate());
	        //System.out.println(strDate);
	        obj.put("actualdate", strDate);
	        obj.put("name", entry.getDescription());
			StringEntity entity = new StringEntity(obj.toString());
			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);

			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addCategoryEntry(CategoryEntry entry) {

		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/addcategoryentry");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);

			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void addOwnerEntry(OwnerEntry entry) {

		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/addownerentry");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);

			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public String addKeywordEntry(KeywordEntry entry) {

		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/addkeywordentry");
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
	        obj.put("categoryid", entry.getCategoryId());
			StringEntity entity = new StringEntity(obj.toString());
			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);

			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return "OK";
	}

	public static void updateCashEntry(CashEntry entry) {

		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/updatecashentry/" + entry.getId());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("description", entry.getDescription());
	        obj.put("amount", entry.getAmount());
	        obj.put("categoryid", entry.getCategoryEntry().getId());
	        obj.put("ownerid", entry.getOwnerEntry().getId());
	        //System.out.println(entry.getActualdate());
	        String strDate = new SimpleDateFormat("yyyy-MM-dd").format(entry.getActualdate());
	        //System.out.println(strDate);
	        obj.put("actualdate", strDate);
	        obj.put("name", entry.getDescription());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteCashEntry(int id) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			System.out.println("Deleting " + id);
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/deletecashentry/" + id);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteCategoryEntry(int id) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			System.out.println("Deleting " + id);
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/deletecategoryentry/" + id);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteOwnerEntry(int id) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			System.out.println("Deleting " + id);
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/deleteownerentry/" + id);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void deleteKeywordEntry(int id) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			System.out.println("Deleting " + id);
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/deletekeywordentry/" + id);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static List<CategoryEntry> getCategoryEntries() {
		CloseableHttpClient client;
		List<CategoryEntry> entries = new ArrayList<CategoryEntry>();
		List<KeywordEntry> keywords = getKeywordEntries();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/categoryentries");
//			Gson gson = new GsonBuilder().setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
//			entries = Arrays.asList(gson.fromJson(reader, CategoryEntry[].class));
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CategoryEntry entry = new CategoryEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (CategoryEntry c : entries) {
			List<KeywordEntry> ks = new ArrayList();
			for (KeywordEntry k : keywords)
				if (k.getCategoryId() == c.getId())
					ks.add(k);
			c.setKeywordEntry(ks);
		}
		return entries;
	}

	public static List<KeywordEntry> getKeywordEntries() {
		CloseableHttpClient client;
		List<KeywordEntry> entries = new ArrayList<KeywordEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/keywordentries");
			// Gson gson = new GsonBuilder().setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				KeywordEntry entry = new KeywordEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				entry.setCategoryId(Integer.parseInt(innerObj.get("categoryid").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}

			// entries = Arrays.asList(gson.fromJson(reader, KeywordEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static List<OwnerEntry> getOwnerEntries() {
		CloseableHttpClient client;
		List<OwnerEntry> entries = new ArrayList<OwnerEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/ownerentries");
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				OwnerEntry entry = new OwnerEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, OwnerEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static List<CashEntry> getEstimateEntries() {
		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/estimateentries");
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static List<CashEntry> getSumByMonth() {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/sumbymonth");
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static List<CashEntry> getSumByOwner() {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/sumbyowner");
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	
	public static CashEntry getCoinbaseBalance() {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/cb/balance");
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(0);
				entry.setAmount(Float.parseFloat(innerObj.get("balance").toString()));
				System.out.println("Balance " + entry.getAmount());
				entries.add(entry);
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0);
	}
	
	public static CashEntry getSumByYear(int year) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/sumbyyear/" + year);
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			//System.out.println(reader.toString());
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0);
	}
	
	public static double getRetainedByYear(int year) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/retainedbyyear/" + year);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			//System.out.println(reader.toString());
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}
	
	public static double getGainedByYear(int year) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/gainedbyyear/" + year);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			//System.out.println(reader.toString());
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}
	
	public static double getSpentByYear(int year) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/spentbyyear/" + year);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			//System.out.println(reader.toString());
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}
	
	public static double getVelocityByYear(int year) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/velocitybyyear/" + year);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			//System.out.println(reader.toString());
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}
	
	public static double getRetainedByMonth(int year,int month) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/retainedbymonth/" + year + "/" + month);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			CashEntry entry = new CashEntry();
			entry.setId(1);
			entry.setAmount(getCoinbaseBalance().getAmount());
			entries.add(entry);
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}
	public static double getSumUpTo(String before) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/sumupto/" + before);
			
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				entries.add(entry);
			}
			
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0).getAmount();
	}

	public static List<CashEntry> getSumByMonthByCategory() {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/sumbymonthbycategory");
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public static List<CashEntry> getSumByMonthByCategory(int year, String sort) {

		CloseableHttpClient client;
		List<CashEntry> entries = new ArrayList<CashEntry>();

		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/reports/top10bycategory/" + year + "/" + sort);
			//Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS")
			//		.registerTypeAdapter(CashEntry.class, new CashEntryDeserializer()).setPrettyPrinting().create();
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				CashEntry entry = new CashEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setDescription(innerObj.get("description").toString());
				entry.setAmount(Float.parseFloat(innerObj.get("amount").toString()));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(Integer.parseInt(innerObj.get("ownerid").toString()));
				owner.setName(innerObj.get("ownername").toString());
				entry.setOwnerEntry(owner);
				CategoryEntry category = new CategoryEntry();
				category.setId(Integer.parseInt(innerObj.get("categoryid").toString()));
				category.setName(innerObj.get("categoryname").toString());
				entry.setCategoryEntry(category);
				entry.setActualdate(new SimpleDateFormat("yyyy-MM-dd").parse(innerObj.get("actualdate").toString()));
				//System.out.println("id " + innerObj.get("id") + " name " + innerObj.get("name") + " catid " + innerObj.get("categoryid"));
				entries.add(entry);
			}
			//entries = Arrays.asList(gson.fromJson(reader, CashEntry[].class));
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	// TODO

	public void updateCategoryEntry(CategoryEntry entry) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/updatecategoryentry/" + entry.getId());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
			StringEntity entity = new StringEntity(obj.toString());
			
			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void updateOwnerEntry(OwnerEntry entry) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/updateownerentry/" + entry.getId());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void updateKeywordEntry(KeywordEntry entry) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/updatekeywordentry/" + entry.getId());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("name", entry.getName());
	        obj.put("categoryid", entry.getCategoryId());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	public void updateEstimateEntry(CashEntry entry) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();

			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/updateestimateentry/" + entry.getId());
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");

			JSONObject obj = new JSONObject();
	        obj.put("amount", entry.getAmount());
			StringEntity entity = new StringEntity(obj.toString());

			httpPost.setEntity(entity);

			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public KeywordEntry getKeyword(String keyword) {

		CloseableHttpClient client;
		List<KeywordEntry> entries = new ArrayList<KeywordEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/keywordentry/" + keyword);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				KeywordEntry entry = new KeywordEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				entries.add(entry);
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0);
	}

	// BACKUP AND RESTORE

	public void takeSnapshot(String type) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/takesnapshot/" + type);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void restoreSnapshot(String name) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/restoresnapshot/" + name);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public List<SnapshotEntry> getLastSnapshotDate() {
		String url = serviceUriRoot + "/lastsnapshotdate";
		return null;
	}

	public List<SnapshotEntry> getSnapshotEntries() {
		CloseableHttpClient client;
		List<SnapshotEntry> entries = new ArrayList<SnapshotEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/snapshotentries");
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				SnapshotEntry entry = new SnapshotEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				entries.add(entry);
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries;
	}

	public SnapshotEntry getSnapshot(int id) {
		CloseableHttpClient client;
		List<SnapshotEntry> entries = new ArrayList<SnapshotEntry>();
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpGet httpGet = new HttpGet(serviceUriRoot + "/v1/snapshotentry/" + id);
			CloseableHttpResponse response;
			response = client.execute(httpGet);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();
				SnapshotEntry entry = new SnapshotEntry();
				entry.setId(Integer.parseInt(innerObj.get("id").toString()));
				entry.setName(innerObj.get("name").toString());
				entries.add(entry);
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return entries.get(0);

	}

	public void deleteSnapshotEntry(String name) {
		try {
			CloseableHttpClient client = HttpClients.custom()
					.setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/deletesnapshotentry/" + name);
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			StringEntity entity = new StringEntity("[]");
			httpPost.setEntity(entity);
			CloseableHttpResponse response = client.execute(httpPost);
			response.close();
			client.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	
	// AUTH
	
	public static String auth(String email,String pass) {
		CloseableHttpClient client;
		String result = "";
		try {
			client = HttpClients.custom().setSSLSocketFactory(SSLUtil.getInsecureSSLConnectionSocketFactory()).build();
			HttpPost httpPost = new HttpPost(serviceUriRoot + "/v1/auth/" + email + "/" + pass);
			CloseableHttpResponse response;
			response = client.execute(httpPost);
			HttpEntity entity = response.getEntity();
			InputStream content = entity.getContent();
			Reader reader = new InputStreamReader(content);
			JSONParser parser = new JSONParser();
			JSONArray ja = (JSONArray) parser.parse(reader);
			Iterator<?> i = ja.iterator();
			while (i.hasNext()) {
				JSONObject innerObj = (JSONObject) i.next();	
				result= innerObj.get("authuser").toString();	
			}
			EntityUtils.consume(entity);
			content.close();
			response.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("auth user: " + result);
		return result;
	}
	
	
	// DONE

	private static class SSLUtil {
		protected static SSLConnectionSocketFactory getInsecureSSLConnectionSocketFactory()
				throws KeyManagementException, NoSuchAlgorithmException {
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(final java.security.cert.X509Certificate[] arg0, final String arg1)
						throws CertificateException {
					// do nothing and blindly accept the certificate
				}

				public void checkServerTrusted(final java.security.cert.X509Certificate[] arg0, final String arg1)
						throws CertificateException {
					// do nothing and blindly accept the server
				}

			} };

			final SSLContext sslcontext = SSLContext.getInstance("SSL");
			sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());

			final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext,
					new String[] { "TLSv1" }, null, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			return sslsf;
		}
	}
}