package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntry;

public class CategoryBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<CategoryEntry> categoryEntries = null;
	List<CategoryEntry> categoryFilters = null;
	CategoryEntry entry = null;
	List<KeywordEntry> keywordEntries = null;
	KeywordEntry keywordEntry = null;
	Integer progress;

	public CategoryBean() {
		entry = new CategoryEntry();
		entry.setId(0);
		entry.setName("");
		keywordEntry = new KeywordEntry();
		keywordEntry.setId(0);
		keywordEntry.setName("");
		refreshFromDB();
	}

	private void refreshFromDB() {
		categoryEntries = client.getCategoryEntries();
		keywordEntries = client.getKeywordEntries();
		for (CategoryEntry c : categoryEntries) {
			List<KeywordEntry> entries = new ArrayList<KeywordEntry>();
			for (KeywordEntry k : keywordEntries) {
				if (c.getId() == k.getCategoryId()) {
					entries.add(k);
				}
			}
			c.setKeywordEntry(entries);
		}
	}

	@PostConstruct
	public void initialize() {
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

	public List<CategoryEntry> getCategoryEntries() {
		return categoryEntries;
	}

	public void setCategoryEntries(List<CategoryEntry> categoryEntries) {
		this.categoryEntries = categoryEntries;
	}

	public List<CategoryEntry> getCategoryFilters() {
		return categoryFilters;
	}

	public void setCategoryFilters(List<CategoryEntry> categoryFilters) {
		this.categoryFilters = categoryFilters;
	}

	public CategoryEntry getEntry() {
		return entry;
	}

	public void setEntry(CategoryEntry entry) {
		this.entry = entry;
	}

	public List<KeywordEntry> getKeywordEntries() {
		return keywordEntries;
	}

	public void setKeywordEntries(List<KeywordEntry> keywordEntries) {
		this.keywordEntries = keywordEntries;
	}

	public KeywordEntry getKeywordEntry() {
		return keywordEntry;
	}

	public void setKeywordEntry(KeywordEntry keywordEntry) {
		this.keywordEntry = keywordEntry;
	}

	public void onComplete() {
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"PROGRESS COMPLETED", "DATA LOADED"));
	}

	public void deleteCategoryEntry() {
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Delete requested for " + entry.getId()
					+ " - " + entry.getName());
			client.deleteCategoryEntry(entry.getId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CATEGORY ENTRY REMOVED", entry.getId() + " - "
									+ entry.getName()));
			categoryEntries.remove(entry);
			refreshFromDB();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}

	public void addCategoryEntry() {
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Add requested for " + entry.getId()
					+ " - " + entry.getName());
			client.addCategoryEntry(entry);
			refreshFromDB();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CATEGORY ENTRY ADDED", entry.getId() + " - "
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

	public void saveCategoryEntry(RowEditEvent event) {
		try {
			CategoryEntry e = (CategoryEntry) event.getObject();
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Save requested for " + e.getId() + " - "
					+ e.getName());
			client.updateCategoryEntry(e);
			refreshFromDB();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CATEGORY ENTRY SAVED", e.getId() + " - "
									+ e.getName()));

		} catch (Exception ex) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + ex.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", ex
							.toString()));
		}

	}

	public void deleteKeywordEntry() {
		try {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Delete requested for "
					+ keywordEntry.getId() + " - " + keywordEntry.getName());
			client.deleteKeywordEntry(keywordEntry.getId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CATEGORY ENTRY REMOVED", keywordEntry.getId()
									+ " - " + keywordEntry.getName()));
			keywordEntries.remove(entry);
			refreshFromDB();
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		}
	}

	public void addKeywordEntry() {
		try {

			for (CategoryEntry category : categoryEntries)				
				if (category.getName().equalsIgnoreCase(entry.getName())) 
					keywordEntry.setCategoryId(category.getId());
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Add requested for "
					+ keywordEntry.getId() + " - " + keywordEntry.getName()
					+ " - " + keywordEntry.getCategoryId());
			client.addKeywordEntry(keywordEntry);
			refreshFromDB();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"KEYWORD ENTRY ADDED", keywordEntry.getId() + " - "
									+ keywordEntry.getName() + " - "
									+ keywordEntry.getCategoryId()));
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
