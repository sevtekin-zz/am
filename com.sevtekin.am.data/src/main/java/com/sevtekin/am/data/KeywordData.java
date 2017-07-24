package com.sevtekin.am.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.sevtekin.am.common.KeywordEntry;


public class KeywordData {
	private static Statement statement = null;
	private static Connection conn = null;
	private DataFacade facade = null;

	protected KeywordData() {
		facade = DataFacade.getInstance();
	}

	protected List<KeywordEntry> select() {
		List<KeywordEntry> entries = new ArrayList<KeywordEntry>();

		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement= "select * from keywordentry ORDER BY name ASC";
			ResultSet results = statement
					.executeQuery(strStatement);
			while (results.next()) {
				KeywordEntry entry = new KeywordEntry();
				entry.setId(results.getInt("id"));
				entry.setName(results.getString("name"));
				entry.setCategoryId(results.getInt("categoryid"));
				entries.add(entry);
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
			+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return entries;
	}

	protected KeywordEntry select(int id) {
		KeywordEntry entry = new KeywordEntry();

		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select * from keywordentry where id=" + id;
			ResultSet results = statement
					.executeQuery(strStatement);
			while (results.next()) {
				entry.setId(results.getInt("id"));
				entry.setName(results.getString("name"));
			}
			results.close();
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
			+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
		return entry;
	}

	protected void insert(KeywordEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "insert into keywordentry (name,categoryid) values ('"
					+ entry.getName() + "', " + entry.getCategoryId() + ")";
			statement.execute(strStatement);
			statement.close();
			conn.close();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM DATA][INFO] " + strStatement);
		} catch (SQLException sqlExcept) {
			sqlExcept.printStackTrace();
		}
	}

	protected void update(KeywordEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "update keywordentry set name='"
					+ entry.getName() + "' where id=" + entry.getId();
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

	protected void delete(int id) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "delete from keywordentry where id=" + id;
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

}
