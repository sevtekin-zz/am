package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;

public class CashFlowBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<String> categories = null;
	List<CashEntry> cashEntries = null;
	List<String> dates = null;
	String[][] sums = null;
	String[] subTotals = null;
	String[] runningTotals = null;

	Integer progress;

	public CashFlowBean() {
		cashEntries = client.getCashEntriesSumByMonthByCategory();
		categories = new ArrayList<String>();
		dates = new ArrayList<String>();
		int i = 0;
		int j = 0;
		int l = cashEntries.size();
		int m = client.getCategoryEntries().size();
		sums = new String[l][m];
		DecimalFormat df = new DecimalFormat("$#.00");
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		String cyear = new SimpleDateFormat("yyyy").format(calendar.getTime());
		Date afterDate = null;
		try {
			afterDate = new SimpleDateFormat("yyyy/MM/dd").parse(cyear
					+ "/01/01");
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		double psum = 0.0;
		for (CashEntry e : cashEntries)
			if (afterDate.after(e.getActualdate()))
				psum += e.getAmount();

		List<CategoryEntry> categoryEntries = client.getCategoryEntries();
		CashEntry carryOverCash = new CashEntry();
		carryOverCash.setActualdate(afterDate);
		carryOverCash.setAmount(psum);
		for (CategoryEntry c : categoryEntries)
			if (c.getName().equalsIgnoreCase("carry over"))
				carryOverCash.setCategoryEntry(c);
		cashEntries.add(carryOverCash);
		for (CashEntry e : cashEntries) {
			if (afterDate.after(e.getActualdate())) {
			} else {
				if (!categories.contains(e.getCategoryEntry().getName()))
					categories.add(e.getCategoryEntry().getName());
				i = categories.indexOf(e.getCategoryEntry().getName());
				String mnth = new SimpleDateFormat("MM").format(e
						.getActualdate());
				String yr = new SimpleDateFormat("yy")
						.format(e.getActualdate());
				String dt = mnth + "'" + yr;
				if (!dates.contains(dt))
					dates.add(dt);
				j = dates.indexOf(dt);
				sums[i][j] = df.format(e.getAmount());
			}
		}
		int k = dates.size();
		subTotals = new String[k];
		runningTotals = new String[k];
		for (i = 0; i < k; i++) {
			double tmp = 0.0;
			for (j = 0; j < l; j++) {
				if (sums[j][i] != null) {
					tmp += Double.parseDouble(sums[j][i].replace("$", ""));
				}
			}
			subTotals[i] = df.format(tmp);
			if (i > 0) {
				double prev = Double.parseDouble(runningTotals[i - 1].replace(
						"$", ""));
				runningTotals[i] = df.format(tmp + prev);
			} else {
				runningTotals[i] = df.format(tmp);
			}
		}
		subTotals[0] = df.format(psum);
	}

	@PostConstruct
	public void initialize() {
	}

	public List<String> getCategories() {
		return categories;
	}

	public void setCategories(List<String> categories) {
		this.categories = categories;
	}

	public List<CashEntry> getCashEntries() {
		return cashEntries;
	}

	public void setCashEntries(List<CashEntry> cashEntries) {
		this.cashEntries = cashEntries;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public String[][] getSums() {
		return sums;
	}

	public void setSums(String[][] sums) {
		this.sums = sums;
	}

	public String[] getSubTotals() {
		return subTotals;
	}

	public void setSubTotals(String[] subTotals) {
		this.subTotals = subTotals;
	}

	public String[] getRunningTotals() {
		return runningTotals;
	}

	public void setRunningTotals(String[] runningTotals) {
		this.runningTotals = runningTotals;
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

}
