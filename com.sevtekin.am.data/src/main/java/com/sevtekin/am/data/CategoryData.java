package com.sevtekin.am.data;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntry;

public class CategoryData {
	private static Statement statement = null;
	private static Connection conn = null;
	private DataFacade facade = null;
	
	protected CategoryData(){
		facade = DataFacade.getInstance();
	}
	protected List<CategoryEntry> select() {
		List<CategoryEntry> entries = new ArrayList<CategoryEntry>();
		
		try {
			conn=facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select * from categoryentry ORDER BY name ASC";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				CategoryEntry entry = new CategoryEntry();
				entry.setId(results.getInt("id"));
				entry.setName(results.getString("name"));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return entries;
	}
	protected CategoryEntry select(int id) {
		CategoryEntry entry = new CategoryEntry();
		try {
			conn=facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select * from categoryentry where id=" + id;
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				entry.setId(results.getInt("id"));
				entry.setName(results.getString("name"));
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return entry;
	}
	
	protected void insert(CategoryEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "insert into categoryentry (name) values ('"
					+ entry.getName() + "')";
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	protected void update(CategoryEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "update categoryentry set name='"
					+ entry.getName() + "' where id=" + entry.getId();
			statement.execute(strStatement);
			statement.close();
			conn.close();
			for (KeywordEntry keyword:entry.getKeywordEntry())
				facade.updateKeywordEntry(keyword);
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][ERROR]");
			e.printStackTrace();
		}
	}

	protected void delete(int id) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "delete from categoryentry where id=" + id;
			statement.execute(strStatement);
			System.out.println(new Timestamp(new Date().getTime())
			+ " [AM DATA][INFO] " + strStatement);
			strStatement = "delete from keywordentry where categoryid=" + id;
			statement.execute(strStatement);
			System.out.println(new Timestamp(new Date().getTime())
			+ " [AM DATA][INFO] " + strStatement);
			statement.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println("[AM DATA][ERROR]");
			e.printStackTrace();
		}
	}

}