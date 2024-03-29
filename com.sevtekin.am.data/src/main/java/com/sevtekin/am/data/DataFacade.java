package com.sevtekin.am.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntry;
import com.sevtekin.am.common.OwnerEntry;
import com.sevtekin.am.common.config.ConfigReader;

public class DataFacade {
	private static DataFacade instance = null;
	private static String connectionString = "";
	private static Connection connection = null;
	private static CashData cashData = new CashData();
	private static CategoryData categoryData = new CategoryData();
	private static KeywordData keywordData = new KeywordData();
	private static OwnerData ownerData = new OwnerData();
	private static ReportData reportData = new ReportData();
	private static ConfigReader configReader = new ConfigReader();
	int count = 1;

	private DataFacade() {
	}

	public static DataFacade getInstance() {
		if (instance == null) {
			instance = new DataFacade();
		}
		return instance;
	}

	protected Connection createConnection() {
		connection = null;
		try {
			String url = configReader.getDBURL();
			String user = configReader.getDBUser();
			String password = configReader.getDBPassword();
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			connection = DriverManager.getConnection(url+"?useSSL=false", user, password);
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] Database connection established " + count++);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][ERROR] Cannot connect to database server ");
			e.printStackTrace();
		}

		return connection;

	}

	public static String getConnectionString() {
		return connectionString;
	}

	public static void setConnectionString(String connectionString) {
		DataFacade.connectionString = connectionString;
	}

	// CASH
	public List<CashEntry> getCashEntries() {
		return cashData.select();
	}
	
	public List<CashEntry> getCashEntries(String filters) {
		return cashData.select(filters);
	}

	public void addCashEntry(CashEntry entry) {
		cashData.insert(entry);
	}
	
	public void updateCashEntry(CashEntry entry) {
		cashData.update(entry);
	}

	public CashEntry getCashEntry(int id) {
		return cashData.select(id);
	}
	
	public void deleteCashEntry(int id) {
		cashData.delete(id);
	}
	
	public List<CashEntry> getEstimateEntries() {
		return cashData.selectEstimates();
	}
	
	public void updateEstimateEntry(CashEntry entry) {
		cashData.updateEstimate(entry);
	}
	
	// CATEGORY
	public List<CategoryEntry> getCategoryEntries() {
		return categoryData.select();
	}

	public CategoryEntry getCategoryEntry(int id) {
		return categoryData.select(id);
	}

	public void addCategoryEntry(CategoryEntry entry) {
		categoryData.insert(entry);
	}
	
	public void updateCategoryEntry(CategoryEntry entry) {
		categoryData.update(entry);
	}
	
	public void deleteCategoryEntry(int id) {
		categoryData.delete(id);
	}

	// KEYWORD
	public List<KeywordEntry> getKeywordEntries() {
		return keywordData.select();
	}

	public KeywordEntry getKeywordEntry(int id) {
		return keywordData.select(id);
	}

	public void addKeywordEntry(KeywordEntry entry) {
		keywordData.insert(entry);
	}
	
	public void updateKeywordEntry(KeywordEntry entry) {
		keywordData.update(entry);
	}
	
	public void deleteKeywordEntry(int id) {
		keywordData.delete(id);
	}
	// OWNER
	public List<OwnerEntry> getOwnerEntries() {
		return ownerData.select();
	}

	public OwnerEntry getOwnerEntry(int id) {
		return ownerData.select(id);
	}
	public void addOwnerEntry(OwnerEntry entry) {
		ownerData.insert(entry);
	}
	
	public void updateOwnerEntry(OwnerEntry entry) {
		ownerData.update(entry);
	}
	
	public void deleteOwnerEntry(int id) {
		ownerData.delete(id);
	}
	
	//REPORT
	
	public List<CashEntry> getCashEntriesSumByMonth() {
		return reportData.selectSumByMonth();
	}
	public List<CashEntry> getCashEntriesSumByMonthByCategory() {
		return reportData.selectSumByMonthByCategory();
	}
	public List<CashEntry> getCashEntriesSumByMonthByCategory(int year,String sort) {
		return reportData.selectSumByMonthByCategory(year,sort);
	}
	public List<CashEntry> getCashEntriesSumByOwner() {
		return reportData.selectSumByOwner();
	}
	public double getCashEntriesSumByYear(int year) {
		return reportData.selectSumByYear(year);
	}
}
