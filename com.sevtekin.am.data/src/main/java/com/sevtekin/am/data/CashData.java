package com.sevtekin.am.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.OwnerEntry;

public class CashData {
	private static Statement statement = null;
	private static Connection conn = null;
	private DataFacade facade = null;

	protected CashData() {
		facade = DataFacade.getInstance();
	}

	protected void insert(CashEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String adate = "NULL";
			if (entry.getActualdate() != null)
				adate = "'" + df.format(entry.getActualdate()) + "'";
			String ddate = df.format(entry.getDuedate());
			String strStatement = "insert into cashentry (categoryid,amount,duedate,actualdate,ownerid,description) values ("
					+ entry.getCategoryEntry().getId()
					+ ","
					+ entry.getAmount()
					+ ",'"
					+ ddate
					+ "',"
					+ adate
					+ ","
					+ entry.getOwnerEntry().getId()
					+ ",'"
					+ entry.getDescription() + "')";
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][ERROR]");
			e.printStackTrace();
		}
	}

	protected void update(CashEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			String adate = "NULL";
			if (entry.getActualdate() != null)
				adate = "'" + df.format(entry.getActualdate()) + "'";
			String ddate = df.format(entry.getDuedate());
			String strStatement = "update cashentry set amount="
					+ entry.getAmount() + ",duedate='" + ddate
					+ "',actualdate=" + adate + ",categoryid="
					+ entry.getCategoryEntry().getId() + ",ownerid="
					+ entry.getOwnerEntry().getId() + " where id="
					+ entry.getId();
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][ERROR]");
			e.printStackTrace();
		}
	}
	
	protected void updateEstimate(CashEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "update estimateentry set amount="
					+ entry.getAmount() + " where id="
					+ entry.getId();
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][ERROR]");
			e.printStackTrace();
		}
	}

	protected List<CashEntry> select() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT c.id as cid,categoryid,amount,duedate,actualdate,ownerid,description,o.name as oname,t.name as tname FROM cashentry as c,ownerentry as o,categoryentry as t where c.ownerid=o.id and c.categoryid=t.id ORDER BY actualdate ASC";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setId(results.getInt("cid"));
				entry.setAmount(results.getDouble("amount"));
				CategoryEntry cat = new CategoryEntry();
				cat.setId(results.getInt("categoryid"));
				cat.setName(results.getString("tname"));
				entry.setCategoryEntry(cat);
				OwnerEntry owner = new OwnerEntry();
				owner.setId(results.getInt("ownerid"));
				owner.setName(results.getString("oname"));
				entry.setOwnerEntry(owner);
				entry.setDuedate(results.getDate("duedate"));
				entry.setActualdate(results.getDate("actualdate"));
				entry.setDescription(results.getString("description"));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected List<CashEntry> select(String filters) {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT c.id as cid,categoryid,amount,duedate,actualdate,ownerid,description,o.name as oname,t.name as tname FROM cashentry as c,ownerentry as o,categoryentry as t where c.ownerid=o.id and c.categoryid=t.id";
			strStatement += filters + " ORDER BY actualdate ASC";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setId(results.getInt("cid"));
				entry.setAmount(results.getDouble("amount"));
				CategoryEntry cat = new CategoryEntry();
				cat.setId(results.getInt("categoryid"));
				cat.setName(results.getString("tname"));
				entry.setCategoryEntry(cat);
				OwnerEntry owner = new OwnerEntry();
				owner.setId(results.getInt("ownerid"));
				owner.setName(results.getString("oname"));
				entry.setOwnerEntry(owner);
				entry.setDuedate(results.getDate("duedate"));
				entry.setActualdate(results.getDate("actualdate"));
				entry.setDescription(results.getString("description"));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

	protected CashEntry select(int id) {
		CashEntry entry = new CashEntry();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT c.id as cid,categoryid,amount,duedate,actualdate,ownerid,description,o.name as oname,t.name as tname FROM cashentry as c,ownerentry as o,categoryentry as t where c.ownerid=o.id and c.categoryid=t.id and c.id="
					+ id;
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				entry.setId(results.getInt("cid"));
				entry.setAmount(results.getDouble("amount"));
				CategoryEntry cat = new CategoryEntry();
				cat.setId(results.getInt("categoryid"));
				cat.setName(results.getString("tname"));
				entry.setCategoryEntry(cat);
				OwnerEntry owner = new OwnerEntry();
				owner.setId(results.getInt("ownerid"));
				owner.setName(results.getString("oname"));
				entry.setOwnerEntry(owner);
				entry.setDuedate(results.getDate("duedate"));
				entry.setActualdate(results.getDate("actualdate"));
				entry.setDescription(results.getString("description"));
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entry;
	}

	protected void delete(int id) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "delete from cashentry where id=" + id;
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
	}
	
	protected List<CashEntry> selectEstimates() {
		List<CashEntry> entries = new ArrayList<CashEntry>();
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "SELECT id,amount FROM estimateentry ORDER BY id ASC";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CashEntry entry = new CashEntry();
				entry.setId(results.getInt("id"));
				entry.setAmount(results.getDouble("amount"));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
		return entries;
	}

}
