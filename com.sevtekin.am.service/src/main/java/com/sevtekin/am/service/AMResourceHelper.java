package com.sevtekin.am.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.data.DataFacade;

public class AMResourceHelper {
	private static DataFacade dataFacade = null;
	private static AMResourceHelper instance = null;
	private List<CashEntry> cashEntries = new ArrayList<CashEntry>();
	private List<CashEntry> sumByMonthEntries = new ArrayList<CashEntry>();

	public AMResourceHelper() {
		dataFacade = DataFacade.getInstance();
	}

	public static AMResourceHelper getInstance() {
		if (instance == null) {
			instance = new AMResourceHelper();
		}
		return instance;
	}

	void gatherData() {
		cashEntries = dataFacade.getCashEntries();
		sumByMonthEntries = dataFacade.getCashEntriesSumByMonth();
	}

	List<String> getYears() {
		List<String> years = new ArrayList<String>();
		for (CashEntry c : sumByMonthEntries)
			if (!years.contains(new SimpleDateFormat("yyyy").format(c.getActualdate())))
				years.add(new SimpleDateFormat("yyyy").format(c.getActualdate()));
		return years;
	}

	double annuallyRetained(int year) {
		double result = 0.0;
		for (CashEntry c : sumByMonthEntries) {
			if ((Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getActualdate())) <= year)) {
				result += c.getAmount();
			}
		}
		return result;
	}

	double annuallyGained(int year) {
		double result = 0.0;
		for (CashEntry c : cashEntries) {
			if ((Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getActualdate())) == year)
					&& (c.getAmount() >= 0) && (!c.getCategoryEntry().getName().equalsIgnoreCase("Carry Over")
							&& (!c.getCategoryEntry().getName().equalsIgnoreCase("Transfer"))))
				result += c.getAmount();
		}
		return result;
	}

	double annuallySpent(int year) {
		double result = 0.0;
		for (CashEntry c : cashEntries) {
			if ((Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getActualdate())) == year)
					&& (c.getAmount() < 0) && (!c.getCategoryEntry().getName().equalsIgnoreCase("Carry Over")
							&& (!c.getCategoryEntry().getName().equalsIgnoreCase("Transfer"))))
				result += c.getAmount();
		}
		return result;
	}

	double annualRetainedAverage() {
		double velocity = 0.0;
		List<String> years = getYears();
		for (String year : years) {
			velocity += annuallyRetained(Integer.parseInt(year));
		}
		return velocity / years.size();
	}

	double annualVelocity() {
		double velocity = 0.0;
		List<String> years = getYears();
		for (String year : years) {
			velocity += annuallyGained(Integer.parseInt(year)) + annuallySpent(Integer.parseInt(year));
			// System.out.println(year + " GAINED " +
			// annuallyGained(Integer.parseInt(year)) + " SPENT " +
			// annuallySpent(Integer.parseInt(year)) + " VELOCITY " +
			// (annuallyGained(Integer.parseInt(year)) +
			// annuallySpent(Integer.parseInt(year))));
		}
		return velocity / years.size();
	}

	double annualVelocity(int year) {
		return annuallyGained(year) + annuallySpent(year);
	}

	double retainedYTD(int year) {
		Calendar now = Calendar.getInstance();
		int diffYear = (now.get(Calendar.YEAR) - year) * -1;
		now.add(Calendar.YEAR, diffYear);
		now.add(Calendar.DATE , 1);
		double result = 0.0;
		for (CashEntry c : cashEntries)
			if (c.getActualdate().before(now.getTime()) && (!c.getCategoryEntry().getName().equalsIgnoreCase("transfer")))
				result += c.getAmount();
		return result;
	}

	double velocityYTD(int year) {
		Date yearStart = new Date();
		try {
			yearStart = new SimpleDateFormat("yyyy/MM/dd").parse(year-1 + "/12/31");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar beforeDate = Calendar.getInstance();
		int diffYear = (beforeDate.get(Calendar.YEAR) - year) * -1;
		beforeDate.add(Calendar.YEAR, diffYear);
		//beforeDate.add(Calendar.DATE, 1);
		double result = 0.00;
		for (CashEntry c : cashEntries) {
			if ((c.getActualdate().after(yearStart) && (c.getActualdate().before(beforeDate.getTime()))
					&& (!c.getCategoryEntry().getName().equalsIgnoreCase("transfer")) && (!c.getCategoryEntry().getName().equalsIgnoreCase("carry over"))))
				result+=c.getAmount();
		}
		
		//System.out.println("Before Date " + beforeDate.getTime() + " Year Start " + yearStart + " Result " + result);
		return result;
	}
	
	double monthlyVelocityYTD(int year){
		Calendar now = Calendar.getInstance();
		int cmonth= now.get(Calendar.MONTH) + 1;
		//System.out.println("YTD VELOCITY FOR " + year + " : " + velocityYTD(year) + " FOR CURRENT MONTH " + cmonth + " DATE " + now.getTime());
		return velocityYTD(year)/cmonth;
	}
	
	double monthlyRetained(int year,int month){
		double result = 0.0;
		for (CashEntry c : sumByMonthEntries){
			Calendar cal = Calendar.getInstance();
			cal.setTime(c.getActualdate());
			if ((cal.get(Calendar.MONTH)==month) && (cal.get(Calendar.YEAR)==year))
				result = c.getAmount();
		}
		return result;
	}
}
