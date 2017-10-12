package com.sevtekin.am.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.OwnerEntry;

public class ReportData {
	private static Statement statement = null;
	private static Connection conn = null;
	private DataFacade facade = null;

	protected ReportData() {
		facade = DataFacade.getInstance();
	}

	protected List<CashEntry> selectSumByMonth() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT * FROM monthlytotals";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setAmount(results.getDouble("amount"));
				String month = results.getString("mnth");
				String year = results.getString("yr");
				Date date = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/" + month + "/01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);
				Date lastDayOfMonth = calendar.getTime();
				entry.setActualdate(lastDayOfMonth);
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException | ParseException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected List<CashEntry> selectSumByMonthByCategory() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT * FROM monthlytotalsbycategory";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setAmount(results.getDouble("amount"));
				CategoryEntry category = new CategoryEntry();
				category.setId(results.getInt("categoryid"));
				category.setName(results.getString("name"));
				entry.setCategoryEntry(category);
				String month = results.getString("mnth");
				String year = results.getString("yr");
				Date date = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/" + month + "/01");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.MONTH, 1);
				calendar.set(Calendar.DAY_OF_MONTH, 1);
				calendar.add(Calendar.DATE, -1);
				Date lastDayOfMonth = calendar.getTime();
				entry.setActualdate(lastDayOfMonth);
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException | ParseException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected List<CashEntry> selectSumByMonthByCategory(int myyear, String sort) {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select sum(amount) as samount,categoryid,name,yr FROM amdb.monthlytotalsbycategory where yr='"
					+ myyear + "' group by categoryid order by samount " + sort + " limit 10";
			System.out.println(strStatement);
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setAmount(results.getDouble("samount"));
				CategoryEntry category = new CategoryEntry();
				category.setId(results.getInt("categoryid"));
				category.setName(results.getString("name"));
				entry.setCategoryEntry(category);
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (Exception e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected List<CashEntry> selectSumByCategory() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT * FROM sumsbycategory";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setAmount(results.getDouble("amount"));
				entry.setCategoryEntry(facade.getCategoryEntry(results.getInt("categoryid")));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (Exception e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected List<CashEntry> selectSumByOwner() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT * FROM totalsbyowner";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setAmount(results.getDouble("amount"));
				OwnerEntry owner = new OwnerEntry();
				owner.setId(results.getInt("ownerid"));
				owner.setName(results.getString("name"));
				entry.setOwnerEntry(owner);
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected double selectSumByYear(int year) {
		double result = 0.0;
		try {
			String start = year + "/01/01";
			String stop = year + "/12/31";
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT sum(amount) as samount FROM cashentry where actualdate>='" + start
					+ "' AND actualdate<='" + stop + "'";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				result = results.getDouble("samount");
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return result;
	}
}
