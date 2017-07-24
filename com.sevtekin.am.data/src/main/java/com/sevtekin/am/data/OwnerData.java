package com.sevtekin.am.data;

import com.sevtekin.am.common.OwnerEntry;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class OwnerData {
	private static Statement statement = null;
	private static Connection conn = null;
	private DataFacade facade = null;

	protected OwnerData() {
		facade = DataFacade.getInstance();
	}

	protected List<OwnerEntry> select() {
		List<OwnerEntry> entries = new ArrayList<OwnerEntry>();

		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select * from ownerentry ORDER BY name ASC";
			ResultSet results = statement.executeQuery(strStatement);
			while (results.next()) {
				OwnerEntry entry = new OwnerEntry();
				entry.setId(results.getInt("id"));
				entry.setName(results.getString("name"));
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

	protected OwnerEntry select(int id) {
		OwnerEntry entry = new OwnerEntry();

		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "select * from ownerentry where id=" + id;
			ResultSet results = statement.executeQuery(strStatement);
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

	protected void insert(OwnerEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "insert into ownerentry (name) values ('"
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

	protected void update(OwnerEntry entry) {
		try {
			conn = facade.createConnection();
			statement = conn.createStatement();
			String strStatement = "update ownerentry set name='"
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
			String strStatement = "delete from ownerentry where id=" + id;
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
