package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.OwnerEntry;

public class OwnerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<OwnerEntry> ownerEntries = null;
	OwnerEntry entry = null;
	Integer progress;

	public OwnerBean() {
		entry = new OwnerEntry();
		ownerEntries = client.getOwnerEntries();
	}

	@PostConstruct
	public void initialize() {
		entry = new OwnerEntry();
		ownerEntries = client.getOwnerEntries();
	}

	public Integer getProgress() {
		if (progress == null)
			progress = 0;
		else {
			progress = progress + (int) (Math.random() * 35);

			if (progress > 100)
				progress = 100;
		}

		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public List<OwnerEntry> getOwnerEntries() {
		return ownerEntries;
	}

	public void setOwnerEntries(List<OwnerEntry> ownerEntries) {
		this.ownerEntries = ownerEntries;
	}

	public OwnerEntry getEntry() {
		return entry;
	}

	public void setEntry(OwnerEntry entry) {
		this.entry = entry;
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"PROGRESS COMPLETED", "DATA LOADED"));
	}

	public void saveOwnerEntry(RowEditEvent event) {
		try {
			entry = (OwnerEntry) event.getObject();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Save requested for " + entry.getId()
					+ " - " + entry.getName());
			client.updateOwnerEntry(entry);
			ownerEntries = client.getOwnerEntries();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"OWNER ENTRY SAVED", entry.getId() + " - "
									+ entry.getName()));
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}

	public void deleteOwnerEntry() {
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Delete requested for " + entry.getId()
					+ " - " + entry.getName());
			client.deleteOwnerEntry(entry.getId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"OWNER ENTRY REMOVED", entry.getId() + " - "
									+ entry.getName()));
			ownerEntries.remove(entry);
			ownerEntries = client.getOwnerEntries();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}

	public void addOwnerEntry() {
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Add requested for " + entry.getId()
					+ " - " + entry.getName());
			client.addOwnerEntry(entry);
			ownerEntries.add(entry);
			ownerEntries = client.getOwnerEntries();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"OWNER ENTRY ADDED", entry.getId() + " - "
									+ entry.getName()));
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}

}
