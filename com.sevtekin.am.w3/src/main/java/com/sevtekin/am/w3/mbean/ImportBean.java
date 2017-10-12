package com.sevtekin.am.w3.mbean;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.context.RequestContext;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.KeywordEntry;
import com.sevtekin.am.common.OwnerEntry;

public class ImportBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<CashEntry> importEntries = null;
	List<CashEntry> selectedEntries = null;
	String selectedKeyword = null;
	List<CategoryEntry> categoryEntries = null;
	List<CategoryEntry> categoryFilters = null;
	List<KeywordEntry> keywordEntries = null;
	CategoryEntry categoryEntry = null;
	CategoryEntry categoryFilter = null;
	CategoryEntry selectedCategory = null;
	List<OwnerEntry> ownerEntries = null;
	List<OwnerEntry> ownerFilters = null;
	OwnerEntry ownerEntry = null;
	OwnerEntry ownerFilter = null;
	CashEntry cashEntry = null;
	List<CashEntry> allEntries = null;
	List<CashEntry> alreadyImported = null;

	public ImportBean() {
		categoryEntries = client.getCategoryEntries();
		ownerEntries = client.getOwnerEntries();
		categoryFilters = client.getCategoryEntries();
		keywordEntries = client.getKeywordEntries();
		ownerFilters = client.getOwnerEntries();
		ownerFilter = new OwnerEntry();
		categoryFilter = new CategoryEntry();
		categoryEntry = new CategoryEntry();
		selectedCategory = new CategoryEntry();
		selectedEntries = null;
		selectedKeyword = null;
		importEntries = new ArrayList<CashEntry>();
		allEntries = client.getCashEntries();
		alreadyImported = new ArrayList<CashEntry>();
		selectedKeyword = "";
	}

	@PostConstruct
	public void initialize() {

	}

	public List<CashEntry> getImportEntries() {
		return importEntries;
	}

	public void setImportEntries(List<CashEntry> importEntries) {
		this.importEntries = importEntries;
	}

	public List<CategoryEntry> getCategoryEntries() {
		return categoryEntries;
	}

	public void setCategoryEntries(List<CategoryEntry> categoryEntries) {
		this.categoryEntries = categoryEntries;
	}

	public List<OwnerEntry> getOwnerEntries() {
		return ownerEntries;
	}

	public void setOwnerEntries(List<OwnerEntry> ownerEntries) {
		this.ownerEntries = ownerEntries;
	}

	public List<CashEntry> getSelectedEntries() {
		return selectedEntries;
	}

	public void setSelectedEntries(List<CashEntry> selectedEntries) {
		this.selectedEntries = selectedEntries;
	}

	public CategoryEntry getCategoryEntry() {
		return categoryEntry;
	}

	public void setCategoryEntry(CategoryEntry categoryEntry) {
		this.categoryEntry = categoryEntry;
	}

	public OwnerEntry getOwnerEntry() {
		return ownerEntry;
	}

	public void setOwnerEntry(OwnerEntry ownerEntry) {
		this.ownerEntry = ownerEntry;
	}

	public CashEntry getCashEntry() {
		return cashEntry;
	}

	public void setCashEntry(CashEntry cashEntry) {
		this.cashEntry = cashEntry;
	}

	public List<CategoryEntry> getCategoryFilters() {
		return categoryFilters;
	}

	public void setCategoryFilters(List<CategoryEntry> categoryFilters) {
		this.categoryFilters = categoryFilters;
	}

	public CategoryEntry getCategoryFilter() {
		return categoryFilter;
	}

	public void setCategoryFilter(CategoryEntry categoryFilter) {
		this.categoryFilter = categoryFilter;
	}

	public List<OwnerEntry> getOwnerFilters() {
		return ownerFilters;
	}

	public void setOwnerFilters(List<OwnerEntry> ownerFilters) {
		this.ownerFilters = ownerFilters;
	}

	public OwnerEntry getOwnerFilter() {
		return ownerFilter;
	}

	public void setOwnerFilter(OwnerEntry ownerFilter) {
		this.ownerFilter = ownerFilter;
	}

	public String getSelectedKeyword() {
		return selectedKeyword;
	}

	public void setSelectedKeyword(String selectedKeyword) {
		this.selectedKeyword = selectedKeyword;
	}

	public void importSelected() {
		// client.takeSnapshot("sn");
		FacesMessage message = new FacesMessage("Successful", "Snapshot taken before import.");
		FacesContext.getCurrentInstance().addMessage(null, message);
		for (CashEntry e : selectedEntries) {
			importEntries.remove(e);
			client.addCashEntry(e);
		}
		message = new FacesMessage("Succesful", selectedEntries.size() + " entries imported");
		FacesContext.getCurrentInstance().addMessage(null, message);
		selectedEntries = null;
	}

	// public void importCashentry() {
	// importEntries.remove(cashEntry);
	// client.addCashEntry(cashEntry);
	// FacesMessage message = new FacesMessage("Succesful",
	// "Cash entry imported");
	// FacesContext.getCurrentInstance().addMessage(null, message);
	// }

	public void applyCategoryToAll() {
		for (CategoryEntry category : categoryEntries)
			if (category.getId() == categoryFilter.getId())
				categoryFilter.setName(category.getName());
		for (CashEntry e : importEntries)
			e.setCategoryEntry(categoryFilter);
	}

	public void applyCategoryToSelected() {
		for (CategoryEntry category : categoryEntries)
			if (category.getId() == categoryFilter.getId())
				categoryFilter.setName(category.getName());
		for (CashEntry e : selectedEntries)
			e.setCategoryEntry(categoryFilter);
	}

	public void applyOwnerToAll() {
		for (OwnerEntry owner : ownerEntries)
			if (owner.getId() == ownerFilter.getId())
				ownerFilter.setName(owner.getName());
		for (CashEntry e : importEntries)
			e.setOwnerEntry(ownerFilter);
	}

	public void applyOwnerToSelected() {
		for (OwnerEntry owner : ownerEntries)
			if (owner.getId() == ownerFilter.getId())
				ownerFilter.setName(owner.getName());
		for (CashEntry e : selectedEntries)
			e.setOwnerEntry(ownerFilter);
	}

	public void removeSelected() {
		for (CashEntry e : selectedEntries)
			importEntries.remove(e);
		FacesMessage message = new FacesMessage("Succesful",
				selectedEntries.size() + " entries removed from import list");
		FacesContext.getCurrentInstance().addMessage(null, message);
		selectedEntries = null;
	}

	public void updateCashEntry(RowEditEvent event) {
		cashEntry = (CashEntry) event.getObject();
		for (CategoryEntry category : categoryEntries)
			if (category.getName().equals(cashEntry.getCategoryEntry().getName()))
				cashEntry.setCategoryEntry(category);
		for (OwnerEntry owner : ownerEntries)
			if (owner.getName().equals(cashEntry.getOwnerEntry().getName()))
				cashEntry.setOwnerEntry(owner);
	}

	public void uploadFiles(FileUploadEvent event) {
		System.out.println("IMPORT 1 --------");
		importEntries = new ArrayList<CashEntry>();
		client = new AMClient();
		try {
			String fileName = event.getFile().getFileName();
			String ownerName = "";
			if (fileName.equalsIgnoreCase("Checking1.csv"))
				ownerName = "WF CHECKING";
			if (fileName.equalsIgnoreCase("Savings3.csv"))
				ownerName = "WF SAVINGS - A";
			if (fileName.equalsIgnoreCase("MarketRate2.csv"))
				ownerName = "WF SAVINGS - B";
			if (fileName.equalsIgnoreCase("CreditCard4.csv"))
				ownerName = "WF CREDIT - A";
			if (fileName.equalsIgnoreCase("CreditCard5.csv"))
				ownerName = "WF CREDIT - B";
			if (fileName.startsWith("Chase3973"))
				ownerName = "CHASE CHECKING";
			if (fileName.startsWith("Chase1855"))
				ownerName = "CHASE SAVINGS";
			if (fileName.startsWith("Chase4775"))
				ownerName = "CHASE CREDIT - A";
			List<OwnerEntry> owners = ownerEntries;
			int ownerId = 0;
			for (OwnerEntry o : owners)
				if (o.getName().equalsIgnoreCase(ownerName))
					ownerId = o.getId();
			String line = "";
			System.out.println("IMPORT 2 --------");
			try {
				InputStream is = event.getFile().getInputstream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				int i = 0;
				while ((line = br.readLine()) != null) {
					if (line.startsWith("Details") || line.startsWith("Type")) {
					} else if (line.contains(",")) {
						String[] fields = line.split(",");
						String strDesc = "";
						String strDate = "";
						String strAmount = "";
						if (fields[0].startsWith("\""))
							fields[0] = fields[0].replace("\"", "");
						strDate = fields[0];
						if (fileName.toLowerCase().startsWith("chase4775")) {
							strDate = fields[1].replace("\"", "");
							// System.out.println(fields[1] + " | " + fields[2] + " | " + fields[3] + " | "
							// + fields[4]);
							strDesc = fields[3].replace("\"", "");
							strAmount = fields[4].replace("\"", "");
						} else if (fileName.toLowerCase().startsWith("chase")) {
							strDate = fields[1].replace("\"", "");
							strDesc = fields[2].replace("\"", "");
							strAmount = fields[3].replace("\"", "");
						} else {
							if (fields[1].startsWith("\""))
								strAmount = fields[1].replace("\"", "");
							if (fields[4].startsWith("\""))
								strDesc = fields[4].replace("\"", "");
						}
						System.out.println("IMPORT 3 --------");
						CashEntry entry = new CashEntry();
						entry.setId(i++);
						Date date = new Date();
						DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
						if (fileName.toLowerCase().startsWith("chase"))
							sdf = new SimpleDateFormat("MM/dd/yy");
						date = sdf.parse(strDate);
						// System.out.println("----" + date);
						entry.setActualdate(date);
						entry.setDuedate(entry.getActualdate());
						entry.setDescription(strDesc.replace("'", ""));
						double charge = 0.0;
						if (!strAmount.equals(""))
							charge = Double.parseDouble(strAmount);
						entry.setAmount(charge);
						System.out.println("IMPORT 4 --------");
						CategoryEntry categoryEntry = suggestCategory(entry.getDescription());
						System.out.println("IMPORT 5 --------");
						entry.setCategoryEntry(categoryEntry);
						OwnerEntry ownerEntry = new OwnerEntry();
						ownerEntry.setId(ownerId);
						ownerEntry.setName(ownerName);
						entry.setOwnerEntry(ownerEntry);
						importEntries.add(entry);
					}
				}

				System.out.println("IMPORT 6 --------");
				DateFormat sdf = new SimpleDateFormat("yyyyMMdd");
				Calendar cal = Calendar.getInstance();
				String today = sdf.format(cal.getTime());
				cal.add(Calendar.MONTH, -4);
				String threeMonthsAgo = sdf.format(cal.getTime());
				String filter = " actualdate>='" + threeMonthsAgo + "' and actualdate<='" + today + "'";
				if (filter != "") {
					filter = filter.trim();
					filter = URLEncoder.encode(filter, "UTF-8");
					filter = filter.replace("+", "%20");
				}
				allEntries = client.getCashEntries(filter);
				System.out.println("FILTER " + filter);
				System.out.println("IMPORT 7 --------");
				System.out.println("COUNT " + allEntries.size());
				alreadyImported = new ArrayList<CashEntry>();
				for (CashEntry e : allEntries)
					for (CashEntry c : importEntries) {
						int d1 = Integer.parseInt(sdf.format(e.getActualdate()));
						int d2 = Integer.parseInt(sdf.format(c.getActualdate()));

						// System.out.println("DATES " + d1 + " | " + d2);
						double a1 = Double.parseDouble(String.format("%.2f", e.getAmount()));
						double a2 = c.getAmount();
						 System.out.println("AMOUNTS " + a1 + " | " + a2);
						int o1 = e.getOwnerEntry().getId();
						int o2 = c.getOwnerEntry().getId();
						// System.out.println("OWNERS " + o1 + " | " + o2);
						String ds1 = e.getDescription();
						String ds2 = c.getDescription();
						// System.out.println("DESCRIPTIONS " + ds1 + " | " + ds2);
						//System.out.println(d1 + " | " + a1 + " | " + o1 + " | " + ds1 + " <> " + d2 + " | " + a2 + " | "
						//		+ o2 + " | " + ds2);
						if ((d1 == d2) && (a1 == a2) && (o1 == o2) && (ds1.equalsIgnoreCase(ds2))) {
							alreadyImported.add(c);
							//System.out.println("MATCH");
						} else {
							//System.out.println("UNMATCH");
						}
						if ((c.getDescription().contains("Pending")) || (c.getDescription().contains("pending"))
								|| (c.getDescription().contains("PENDING")))
							alreadyImported.add(c);

					}
				for (CashEntry a : alreadyImported) {
					importEntries.remove(a);
					System.out.println("ALREADY IMPORTED " + a.getAmount() + " | " + a.getActualdate() + " | "
							+ a.getDescription());
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				FacesMessage message = new FacesMessage("Succesful", event.getFile().getFileName() + " is uploaded.");
				FacesContext.getCurrentInstance().addMessage(null, message);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}

	private CategoryEntry suggestCategory(String description) {
		CategoryEntry entry = new CategoryEntry();
		for (CategoryEntry category : categoryFilters)
			if (category.getName().equalsIgnoreCase("Miscellaneous"))
				entry.setId(category.getId());
		for (KeywordEntry e : keywordEntries)
			if (description.toLowerCase().contains(e.getName().toLowerCase()))
				entry.setId(e.getCategoryId());
		for (CategoryEntry c : categoryFilters)
			if (c.getId() == entry.getId())
				entry.setName(c.getName());
		return entry;
	}

	public void addKeyword(SelectEvent event) {
		cashEntry = (CashEntry) event.getObject();
		selectedKeyword = cashEntry.getDescription();
		selectedCategory.setId(cashEntry.getCategoryEntry().getId());
		selectedCategory.setName(cashEntry.getCategoryEntry().getName());
		RequestContext.getCurrentInstance().execute("PF('keywordDialog').show()");
	}

	public void addKeywordEntry() {
		System.out.println(
				"ADD --- " + selectedKeyword + " - " + selectedCategory.getId() + " - " + selectedCategory.getName());
		KeywordEntry entry = new KeywordEntry();
		entry.setName(selectedKeyword);
		entry.setCategoryId(selectedCategory.getId());
		String ret = client.addKeywordEntry(entry);
		if (ret.equals("INCLUDED"))
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!",
					"Keyword " + selectedKeyword + " already included in another keyword"));
		else if (ret.equals("INCLUDES"))
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Warning!",
					"Keyword " + selectedKeyword + " includes another keyword"));
		else {
			categoryEntries = client.getCategoryEntries();
			keywordEntries = client.getKeywordEntries();
			for (CashEntry c : importEntries)
				if (c.getDescription().contains(selectedKeyword))
					c.setCategoryEntry(suggestCategory(c.getDescription()));
			// RequestContext.getCurrentInstance().update("cashTable");
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Successful", "Keyword " + selectedKeyword + " added to " + selectedCategory.getName()));
		}
	}

	public CategoryEntry getSelectedCategory() {
		return selectedCategory;
	}

	public void setSelectedCategory(CategoryEntry selectedCategory) {
		this.selectedCategory = selectedCategory;
	}

}
