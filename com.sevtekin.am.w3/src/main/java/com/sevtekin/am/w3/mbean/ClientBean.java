package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.OwnerEntry;

public class ClientBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<CategoryEntry> categoryEntries = null;
	List<CategoryEntry> categoryFilters = null;
	List<CashEntry> cashEntries = null;
	List<CashEntry> selectedCashEntries = null;
	CategoryEntry categoryEntry = null;
	CashEntry cashEntry = null;
	List<OwnerEntry> ownerEntries = null;
	List<OwnerEntry> ownerFilters = null;
	OwnerEntry ownerEntry = null;
	Double runningTotal = 0.0;
	String equalsAmount = "";
	String greaterAmount = "";
	String lesserAmount = "";
	String description= "";
	Date equalsDueDate = null;
	Date afterDueDate = null;
	Date beforeDueDate = null;
	Date equalsActualDate = null;
	Date afterActualDate = null;
	Date beforeActualDate = null;
	Integer progress;
	
	public ClientBean() {
		cashEntry = new CashEntry();
		categoryEntry = new CategoryEntry();
		categoryEntry.setId(0);
		categoryEntry.setName("SELECT");
		cashEntry.setCategoryEntry(categoryEntry);
		ownerEntry = new OwnerEntry();
		ownerEntry.setId(0);
		ownerEntry.setName("SELECT");
		cashEntry.setOwnerEntry(ownerEntry);
		runningTotal = 0.0;
		categoryEntries = client.getCategoryEntries();
		categoryEntry.setName("SELECT");
		categoryFilters = new ArrayList<CategoryEntry>();
		CategoryEntry category = new CategoryEntry();
		category.setId(0);
		category.setName("SELECT");
		categoryFilters.add(category);
		for (CategoryEntry c : getCategoryEntries())
			categoryFilters.add(c);
		ownerEntries = client.getOwnerEntries();
		ownerFilters = new ArrayList<OwnerEntry>();
		OwnerEntry owner = new OwnerEntry();
		owner.setId(0);
		owner.setName("SELECT");
		ownerFilters.add(owner);
		for (OwnerEntry o : getOwnerEntries())
			ownerFilters.add(o);
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		afterActualDate = calendar.getTime();
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.DATE, -1);
		beforeActualDate = calendar.getTime();
		progress = 0;
		selectedCashEntries = null;
		refreshFromDBWithFilters();
	}

	@PostConstruct
	public void initialize() {
	}
	
	public void resetFilters() {
		equalsActualDate = null;
		afterActualDate = null;
		beforeActualDate = null;
		equalsDueDate = null;
		afterDueDate = null;
		beforeDueDate = null;
		equalsAmount = null;
		greaterAmount = null;		
		lesserAmount = null;
		categoryEntry.setName("SELECT");
		categoryEntry.setId(0);
		ownerEntry.setName("SELECT");
		ownerEntry.setId(0);
		runningTotal = 0.0;
		progress = 0;
	}

	public void refreshFromDBWithFilters() {
		String filters = " description like " + description;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (!(this.getEqualsAmount().equals(null)) && (!this.getEqualsAmount().equals("")) && (!this.getEqualsAmount().isEmpty()))
				filters += " and amount=" + getEqualsAmount();
			if (!(this.getGreaterAmount().equals(null)) && (!this.getGreaterAmount().equals("")) && (!this.getGreaterAmount().isEmpty()))
				filters += " and amount>" + getGreaterAmount();
			if (!(this.getLesserAmount().equals(null)) && (!this.getLesserAmount().equals("")) && (!this.getLesserAmount().isEmpty()))
				filters += " and amount<" + getLesserAmount();
			if (getEqualsDueDate() != null)
				filters += " and duedate='" + df.format(getEqualsDueDate())
						+ "'";
			if (getBeforeDueDate() != null)
				filters += " and duedate<='" + df.format(getBeforeDueDate())
						+ "'";
			if (getAfterDueDate() != null)
				filters += " and duedate>='" + df.format(getAfterDueDate())
						+ "'";
			if (getEqualsActualDate() != null)
				filters += " and actualdate='"
						+ df.format(getEqualsActualDate()) + "'";
			if (getBeforeActualDate() != null)
				filters += " and actualdate<='"
						+ df.format(getBeforeActualDate()) + "'";
			if (getAfterActualDate() != null)
				filters += " and actualdate>='"
						+ df.format(getAfterActualDate()) + "'";
			if (getCategoryEntry().getId() != 0)
				filters += " and categoryid=" + getCategoryEntry().getId();
			if (getOwnerEntry().getId() != 0)
				filters += " and ownerid=" + getOwnerEntry().getId();
			if (filters != "") {
				cashEntries = client.getCashEntries(filters);
				System.out.println(new Timestamp(new Date().getTime())
						+ " [AM W3] [INFO] DATA LOADED " + filters);
			} else {
				cashEntries = client.getCashEntries();
			}
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		} finally {
			runningTotal = 0.0;
		}
	}

	public List<CashEntry> getCashEntries() {
		return cashEntries;
	}

	public CashEntry getCashEntry() {
		return cashEntry;
	}

	public void setCashEntry(CashEntry cashEntry) {
		this.cashEntry = cashEntry;
	}

	public void setCashEntries(List<CashEntry> entries) {
		this.cashEntries = entries;
	}

	public List<CashEntry> getSelectedCashEntries() {
		return selectedCashEntries;
	}

	public void setSelectedCashEntries(List<CashEntry> selectedCashEntries) {
		this.selectedCashEntries = selectedCashEntries;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	

	// Progress

	public String getDescription() {
		return description;
	}

	

	public Integer getProgress() {
		if (progress == null)
			progress = 0;
		else {
			progress = progress + (int) (Math.random() * 45);

			if (progress > 100)
				progress = 100;
		}

		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}

	public void onComplete() {
		System.out.println("COMPLETED!!!!!!!!");
		FacesContext.getCurrentInstance().addMessage(
				null,
				new FacesMessage(FacesMessage.SEVERITY_INFO,
						"PROGRESS COMPLETED", "DATA LOADED"));
	}

	public void deleteCashEntry() {
		try {
			for (CategoryEntry category : categoryEntries)
				if (category.getName().equals(
						cashEntry.getCategoryEntry().getName()))
					cashEntry.setCategoryEntry(category);
			for (OwnerEntry owner : ownerEntries)
				if (owner.getName().equals(cashEntry.getOwnerEntry().getName()))
					cashEntry.setOwnerEntry(owner);
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Delete requested for "
					+ cashEntry.getId() + " - " + cashEntry.getAmount() + " - "
					+ cashEntry.getDuedate() + " - "
					+ cashEntry.getActualdate() + " - "
					+ cashEntry.getCategoryEntry().getId() + " - "
					+ cashEntry.getCategoryEntry().getName() + " - "
					+ cashEntry.getOwnerEntry().getId() + " - "
					+ cashEntry.getOwnerEntry().getName());
			client.deleteCashEntry(cashEntry.getId());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CASH ENTRY REMOVED", cashEntry.getId() + " - "
									+ cashEntry.getAmount() + " - "
									+ cashEntry.getDuedate() + " - "
									+ cashEntry.getActualdate() + " - "
									+ cashEntry.getCategoryEntry().getId()
									+ " - "
									+ cashEntry.getCategoryEntry().getName()
									+ " - " + cashEntry.getOwnerEntry().getId()
									+ " - "
									+ cashEntry.getOwnerEntry().getName()));
			cashEntries.remove(cashEntry);
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] ERROR: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		} finally {
			runningTotal = 0.0;
		}
	}

	public void addCashEntry() {
		try {
			for (CategoryEntry category : categoryEntries)
				if (category.getName().equals(
						cashEntry.getCategoryEntry().getName()))
					cashEntry.setCategoryEntry(category);
			for (OwnerEntry owner : ownerEntries)
				if (owner.getName().equals(cashEntry.getOwnerEntry().getName()))
					cashEntry.setOwnerEntry(owner);
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Add requested for " + cashEntry.getId()
					+ " - " + cashEntry.getAmount() + " - "
					+ cashEntry.getDuedate() + " - "
					+ cashEntry.getActualdate() + " - "
					+ cashEntry.getCategoryEntry().getId() + " - "
					+ cashEntry.getCategoryEntry().getName() + " - "
					+ cashEntry.getOwnerEntry().getId() + " - "
					+ cashEntry.getOwnerEntry().getName());
			cashEntry.setDuedate(cashEntry.getActualdate());
			client.addCashEntry(cashEntry);
			refreshFromDBWithFilters();
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CASH ENTRY ADDED", cashEntry.getId() + " - "
									+ cashEntry.getAmount() + " - "
									+ cashEntry.getDuedate() + " - "
									+ cashEntry.getActualdate() + " - "
									+ cashEntry.getCategoryEntry().getId()
									+ " - "
									+ cashEntry.getCategoryEntry().getName()
									+ " - " + cashEntry.getOwnerEntry().getId()
									+ " - "
									+ cashEntry.getOwnerEntry().getName()));
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		} finally {
			runningTotal = 0.0;
		}
	}

	public void saveCashEntry(RowEditEvent event) {
		try {
			CashEntry entry = (CashEntry) event.getObject();
			for (CategoryEntry category : categoryEntries)
				if (category.getName().equals(
						entry.getCategoryEntry().getName()))
					entry.setCategoryEntry(category);
			for (OwnerEntry owner : ownerEntries)
				if (owner.getName().equals(entry.getOwnerEntry().getName()))
					entry.setOwnerEntry(owner);
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3][INFO] Save requested for " + entry.getId()
					+ " - " + entry.getAmount() + " - " + entry.getDuedate()
					+ " - " + entry.getActualdate() + " - "
					+ entry.getCategoryEntry().getId() + " - "
					+ entry.getCategoryEntry().getName() + " - "
					+ entry.getOwnerEntry().getId() + " - "
					+ entry.getOwnerEntry().getName());
			client.updateCashEntry(entry);
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_INFO,
							"CASH ENTRY SAVED", entry.getId() + " - "
									+ entry.getAmount() + " - "
									+ entry.getDuedate() + " - "
									+ entry.getActualdate() + " - "
									+ entry.getCategoryEntry().getId() + " - "
									+ entry.getCategoryEntry().getName()
									+ " - " + entry.getOwnerEntry().getId()
									+ " - " + entry.getOwnerEntry().getName()));
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime())
					+ " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
							.toString()));
		} finally {
			runningTotal = 0.0;
		}

	}

	public Double getRunningTotal(Double amount) {
		return runningTotal += amount;
	}

	public void resetRunningTotal() {
		runningTotal = 0.0;
	}

	// Category

	public String getEqualsAmount() {
		return equalsAmount;
	}

	public void setEqualsAmount(String equalsAmount) {
		this.equalsAmount = equalsAmount;
	}

	public String getGreaterAmount() {
		return greaterAmount;
	}

	public void setGreaterAmount(String greaterAmount) {
		this.greaterAmount = greaterAmount;
	}

	public String getLesserAmount() {
		return lesserAmount;
	}

	public void setLesserAmount(String lesserAmount) {
		this.lesserAmount = lesserAmount;
	}

	public Date getEqualsDueDate() {
		return equalsDueDate;
	}

	public void setEqualsDueDate(Date equalsDueDate) {
		this.equalsDueDate = equalsDueDate;
	}

	public Date getAfterDueDate() {
		return afterDueDate;
	}

	public void setAfterDueDate(Date afterDueDate) {
		this.afterDueDate = afterDueDate;
	}

	public Date getBeforeDueDate() {
		return beforeDueDate;
	}

	public void setBeforeDueDate(Date beforeDueDate) {
		this.beforeDueDate = beforeDueDate;
	}

	public Date getEqualsActualDate() {
		return equalsActualDate;
	}

	public void setEqualsActualDate(Date equalsActualDate) {
		this.equalsActualDate = equalsActualDate;
	}

	public Date getAfterActualDate() {
		return afterActualDate;
	}

	public void setAfterActualDate(Date afterActualDate) {
		this.afterActualDate = afterActualDate;
	}

	public Date getBeforeActualDate() {
		return beforeActualDate;
	}

	public void setBeforeActualDate(Date beforeActualDate) {
		this.beforeActualDate = beforeActualDate;
	}

	public List<CategoryEntry> getCategoryFilters() {
		return categoryFilters;
	}

	public void setCategoryFilters(List<CategoryEntry> categoryFilters) {
		this.categoryFilters = categoryFilters;
	}

	public List<OwnerEntry> getOwnerFilters() {
		return ownerFilters;
	}

	public void setOwnerFilters(List<OwnerEntry> ownerFilters) {
		this.ownerFilters = ownerFilters;
	}

	public List<CategoryEntry> getCategoryEntries() {
		return categoryEntries;
	}

	public void setCategoryEntries(List<CategoryEntry> entries) {
		this.categoryEntries = entries;
	}

	public CategoryEntry getCategoryEntry() {
		return categoryEntry;
	}

	public void setCategoryEntry(CategoryEntry entry) {
		this.categoryEntry = entry;
	}

	// Owners

	public OwnerEntry getOwnerEntry() {
		return ownerEntry;
	}

	public void setOwnerEntry(OwnerEntry entry) {
		this.ownerEntry = entry;
	}

	public List<OwnerEntry> getOwnerEntries() {
		return ownerEntries;
	}

	public void setOwnerEntries(List<OwnerEntry> ownerEntries) {
		this.ownerEntries = ownerEntries;
	}

//	public void saveOwnerEntry(RowEditEvent event) {
//		try {
//			OwnerEntry entry = (OwnerEntry) event.getObject();
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3][INFO] Save requested for " + entry.getId()
//					+ " - " + entry.getName());
//			client.updateOwnerEntry(entry);
//			initialize();
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_INFO,
//							"OWNER ENTRY SAVED", entry.getId() + " - "
//									+ entry.getName()));
//		} catch (Exception e) {
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3] [ERROR] " + e.getMessage());
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
//							.toString()));
//		}
//	}
//
//	public void deleteOwnerEntry() {
//		try {
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3][INFO] Delete requested for "
//					+ ownerEntry.getId() + " - " + ownerEntry.getName());
//			client.deleteOwnerEntry(ownerEntry.getId());
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_INFO,
//							"OWNER ENTRY REMOVED", ownerEntry.getId() + " - "
//									+ ownerEntry.getName()));
//			ownerEntries.remove(ownerEntry);
//		} catch (Exception e) {
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3] ERROR: " + e.getMessage());
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
//							.toString()));
//		}
//	}
//
//	public void addOwnerEntry() {
//		try {
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3][INFO] Add requested for " + ownerEntry.getId()
//					+ " - " + ownerEntry.getName());
//			client.addOwnerEntry(ownerEntry);
//			initialize();
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_INFO,
//							"OWNER ENTRY ADDED", ownerEntry.getId() + " - "
//									+ ownerEntry.getName()));
//		} catch (Exception e) {
//			System.out.println(new Timestamp(new Date().getTime())
//					+ " [AM W3] [ERROR] " + e.getMessage());
//			FacesContext.getCurrentInstance().addMessage(
//					null,
//					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e
//							.toString()));
//		}
//	}
}
