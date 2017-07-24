package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.SnapshotEntry;

public class SnapshotBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<SnapshotEntry> snapshotEntries = null;
	SnapshotEntry snapshotEntry = null;

	public SnapshotBean() {
		snapshotEntries = client.getSnapshotEntries();
		snapshotEntry = new SnapshotEntry();
	}

	@PostConstruct
	public void initialize() {
		snapshotEntries = client.getSnapshotEntries();
		snapshotEntry = new SnapshotEntry();
	}

	public List<SnapshotEntry> getSnapshotEntries() {
		return snapshotEntries;
	}

	public void setSnapshotEntries(List<SnapshotEntry> snapshotEntries) {
		this.snapshotEntries = snapshotEntries;
	}
	
	public SnapshotEntry getSnapshotEntry() {
		return snapshotEntry;
	}

	public void setSnapshotEntry(SnapshotEntry snapshotEntry) {
		this.snapshotEntry = snapshotEntry;
	}

	public void takeSnapshot(){
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Snapshot requested");
			client.takeSnapshot("mn");
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SNAPSHOT TAKEN", ""));
			snapshotEntries = client.getSnapshotEntries();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}
	
	public void restoreSnapshot(){
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Snapshot restore requested for " + snapshotEntry.getName());
			client.restoreSnapshot(snapshotEntry.getName());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SNAPSHOT RESTORED", snapshotEntry.getName()));
			snapshotEntries = client.getSnapshotEntries();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}
	public void deleteSnapshot(){
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Delete requested for "
					+ snapshotEntry.getId() + " - " + snapshotEntry.getName());
			client.deleteSnapshotEntry(snapshotEntry.getName());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"SNAPSHOT REMOVED", snapshotEntry.getName()));
			snapshotEntries.remove(snapshotEntry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}
}
