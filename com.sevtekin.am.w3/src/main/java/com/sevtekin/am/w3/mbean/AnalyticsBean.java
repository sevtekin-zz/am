package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;
import com.sevtekin.am.common.OwnerEntry;

public class AnalyticsBean implements Serializable {

	private static final long serialVersionUID = 1L;
	//AMClient client = new AMClient();
	List<CashEntry> result = new ArrayList<CashEntry>();
	private HorizontalBarChartModel sumByMonthByCategoryChart;
	List<CashEntry> entries = null;
	double wfc = 0.0;
	double wfsa = 0.0;
	double wfsb = 0.0;
	double wfcca = 0.0;
	double wfccb = 0.0;
	double cc = 0.0;
	double ccca = 0.0;
	double cs = 0.0;
	double cb = 0.0;
	double total = 0.0;
	double grandTotal = 0.0;

	List<CashEntry> sumByMonthEntries = null;
	List<CashEntry> sumByMonthByCategoryEntries = null;
	List<CashEntry> estimateEntries = null;
	List<CashEntry> top10Gains = null;
	List<CashEntry> top10Losses = null;
	List<CashEntry> accountSums = null;
	List<CashEntry> velocities = null;
	List<CashEntry> annuallySpentGained = null;
	//List<CashEntry> cashEntries = null;
	private LineChartModel trendsChart;
	private Date beforeDate = null;
	private Date afterDate = null;
	private int year;
	private double thisYearsVelocity = 0.0;
	private double lastYearsVelocity = 0.0;
	private double monthlyVelocity = 0.0;
	private double thisMonthsVelocity = 0.0;
	private double lastMonthsVelocity = 0.0;
	private String velocityIndicator = "up";
	private String entertainmentIndicator = "up";
	private String foodIndicator = "up";
	private String vacationIndicator = "up";
	private String snacksIndicator = "up";
	private String horseCareIndicator = "up";
	private String carMaintenanceIndicator = "up";
	private String estimateIndicator = "up";
	private String dailyIndicator = "up";
	private double monthlyVelocityGoal = 2500.00;
	private double monthlyVelocityGoalDiff = 0.0;
	private double entertainmentGoal = -7000.00;
	private double entertainmentGoalDiff = 0.0;
	private double foodGoal = -6000.00;
	private double foodGoalDiff = 0.0;
	private double snacksGoal = -2000.00;
	private double snacksGoalDiff = 0.0;
	private double vacationGoal = -7000.00;
	private double vacationGoalDiff = 0.0;
	private double horseCareGoal = -2500.00;
	private double horseCareGoalDiff = 0.0;
	private double carMaintenanceGoal = -4000.00;
	private double carMaintenanceGoalDiff = 0.0;
	private double estimateGoalDiff = 0.0;
	private double dailyVelocityDiff = 0.0;
	
	

	@PostConstruct
	public void initialize() {
	
		
		int cmonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int cyear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		List<CategoryEntry> cats = AMClient.getCategoryEntries();
		List<CashEntry> sums = AMClient.getSumByMonthByCategory();
		sumByMonthByCategoryEntries = sums;
		estimateEntries = AMClient.getEstimateEntries();
		sumByMonthEntries = AMClient.getSumByMonth();
		//cashEntries = client.getCashEntries();
		Date earliest = new Date();
		for (CashEntry e : sums)
			if (earliest.after(e.getActualdate()))
				earliest = e.getActualdate();
		int eyear = Integer.parseInt(new SimpleDateFormat("yyyy").format(earliest));
		int emonth = Integer.parseInt(new SimpleDateFormat("MM").format(earliest));
		int months = (cyear - eyear) * 12 + cmonth - emonth;

		for (CategoryEntry cat : cats) {
			if ((!cat.getName().equalsIgnoreCase("Carry Over")) && (!cat.getName().equalsIgnoreCase("Transfer"))) {
				CashEntry e = new CashEntry();
				e.setCategoryEntry(cat);
				e.setAmount(0.0);
				e.setDescription("LAST YEAR SAME MONTH");
				result.add(e);
				e = new CashEntry();
				e.setCategoryEntry(cat);
				e.setAmount(0.0);
				e.setDescription("CURRENT MONTH");
				result.add(e);
				e = new CashEntry();
				e.setCategoryEntry(cat);
				e.setAmount(0.0);
				e.setDescription("LAST MONTH");
				result.add(e);
				e = new CashEntry();
				e.setCategoryEntry(cat);
				e.setAmount(0.0);
				e.setDescription("AVERAGE");
				result.add(e);
			}
		}
        System.out.println("1--------------");
		for (CashEntry cash : sums)
			if (cash.getAmount() < 0)
				cash.setAmount(cash.getAmount() * -1);
		for (CategoryEntry cat : cats) {
			double amount = 0.0;
			for (CashEntry cash : sums) {
				int month = Integer.parseInt(new SimpleDateFormat("MM").format(cash.getActualdate()));
				int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(cash.getActualdate()));
				if (cash.getCategoryEntry().getId() == cat.getId())
					amount += cash.getAmount();
				if ((cyear == year) && (cmonth == month + 1) && (cash.getCategoryEntry().getId() == cat.getId())) {
					CashEntry e = new CashEntry();
					e.setCategoryEntry(cash.getCategoryEntry());
					e.setAmount(cash.getAmount());
					e.setDescription("LAST MONTH");
					if ((!e.getCategoryEntry().getName().equalsIgnoreCase("Carry Over"))
							&& (!e.getCategoryEntry().getName().equalsIgnoreCase("Transfer")))
						result.add(e);
				}
				if ((cyear == year) && (cmonth == month) && (cash.getCategoryEntry().getId() == cat.getId())) {
					CashEntry e = new CashEntry();
					e.setCategoryEntry(cash.getCategoryEntry());
					e.setAmount(cash.getAmount());
					e.setDescription("CURRENT MONTH");
					if ((!e.getCategoryEntry().getName().equalsIgnoreCase("Carry Over"))
							&& (!e.getCategoryEntry().getName().equalsIgnoreCase("Transfer")))
						result.add(e);
				}
				if ((cyear - 1 == year) && (cmonth == month) && (cash.getCategoryEntry().getId() == cat.getId())) {
					CashEntry e = new CashEntry();
					e.setCategoryEntry(cash.getCategoryEntry());
					e.setAmount(cash.getAmount());
					e.setDescription("LAST YEAR SAME MONTH");
					if ((!e.getCategoryEntry().getName().equalsIgnoreCase("Carry Over"))
							&& (!e.getCategoryEntry().getName().equalsIgnoreCase("Transfer")))
						result.add(e);
				}
			}
			if ((!cat.getName().equalsIgnoreCase("Carry Over")) && (!cat.getName().equalsIgnoreCase("Transfer"))) {
				CashEntry e = new CashEntry();
				e = new CashEntry();
				e.setCategoryEntry(cat);
				e.setAmount(amount / months);
				e.setDescription("AVERAGE");
				result.add(e);
			}
		}
		
		System.out.println("2--------------");

		createSumByMonthByCategoryChart();
		
		System.out.println("3--------------");
		
		entries = AMClient.getSumByOwner();
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		year = calendar.get(Calendar.YEAR);
		cb = AMClient.getCoinbaseBalance().getAmount();

		for (CashEntry entry : entries) {
			String owner = entry.getOwnerEntry().getName();
			if (owner.equalsIgnoreCase("WF CHECKING"))
				wfc = entry.getAmount();
			else if (owner.equalsIgnoreCase("WF SAVINGS - A"))
				wfsa = entry.getAmount();
			else if (owner.equalsIgnoreCase("WF SAVINGS - B"))
				wfsb = entry.getAmount();
			else if (owner.equalsIgnoreCase("WF CREDIT - A"))
				wfcca = entry.getAmount();
			else if (owner.equalsIgnoreCase("WF CREDIT - B"))
				wfccb = entry.getAmount();
			else if (owner.equalsIgnoreCase("CHASE CHECKING"))
				cc = entry.getAmount();
			else if (owner.equalsIgnoreCase("CHASE CREDIT - A"))
				ccca = entry.getAmount();
			else if (owner.equalsIgnoreCase("CHASE SAVINGS"))
				cs = entry.getAmount();
		}
		
		total = wfc + wfsa + wfsb + wfcca + wfccb + cc + ccca + cs + cb;
		
		System.out.println("4--------------");
		
		
		accountSums = new ArrayList<CashEntry>();
		CashEntry accountSum = new CashEntry();
		OwnerEntry account = new OwnerEntry();
		account.setName("WF CHECKING");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(wfc);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("WF SAVINGS - A");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(wfsa);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("WF SAVINGS - B");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(wfsb);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("WF CREDIT - A");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(wfcca);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("WF CREDIT - B");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(wfccb);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("CHASE CHECKING");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(cc);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("CHASE CREDIT - A");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(ccca);
		accountSums.add(accountSum);

		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("CHASE SAVINGS");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(cs);
		accountSums.add(accountSum);
		
		accountSum = new CashEntry();
		account = new OwnerEntry();
		account.setName("COINBASE ASSETS");
		accountSum.setOwnerEntry(account);
		accountSum.setAmount(cb);
		accountSums.add(accountSum);
		
		System.out.println("5--------------");

		List<CashEntry> tmp = AMClient.getSumByMonthByCategory(year, "desc");
		top10Gains = new ArrayList<CashEntry>();
		for (CashEntry e : tmp)
			if (e.getAmount() > 0)
				top10Gains.add(e);

		List<CashEntry> tmp2 = AMClient.getSumByMonthByCategory(year, "asc");
		top10Losses = new ArrayList<CashEntry>();
		for (CashEntry e : tmp2)
			if (e.getAmount() < 0)
				top10Losses.add(e);
		beforeDate = calendar.getTime();
		year = calendar.get(Calendar.YEAR);

		try {

			beforeDate = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/12/31");
			afterDate = beforeDate;
			for (CashEntry e : sumByMonthEntries)
				if (e.getActualdate().before(afterDate))
					afterDate = e.getActualdate();
			String mnth = new SimpleDateFormat("MM").format(afterDate);
			String yr = new SimpleDateFormat("yy").format(afterDate);
			afterDate = new SimpleDateFormat("yyyy/MM/dd").parse(yr + "/" + mnth + "/1");

			createTrendsChart();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		
		System.out.println("6--------------");
		
		
		velocities = new ArrayList<CashEntry>();

		int ccmonth = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int ccyear = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		
		CashEntry velocity = new CashEntry();
		velocity.setDescription("THIS MONTH RETAINED");
		velocity.setAmount(calculateMonthlyRetained(ccyear,ccmonth));
		velocities.add(velocity);
		
		System.out.println("7--------------");

		velocity = new CashEntry();
		velocity.setDescription("LAST MONTH RETAINED");
		velocity.setAmount(calculateMonthlyRetained(ccyear,ccmonth- 1));
		velocities.add(velocity);
		
		System.out.println("8--------------");

		velocity = new CashEntry();
		velocity.setDescription("MONTHLY VELOCITY");
		int numOfMonths  = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		//System.out.println("num of months " + numOfMonths);
		//double amount = client.getSumByYear(year).getAmount();
		//System.out.println("amount "  + amount);
		velocity.setAmount(AMClient.getSumByYear(year).getAmount()/numOfMonths);
		velocities.add(velocity);
		
		System.out.println("9--------------");

		velocity = new CashEntry();
		velocity.setDescription("THIS YEAR RETAINED ");
		velocity.setAmount(calculateYearlyRetained(year) + cb);
		velocities.add(velocity);
		
		System.out.println("10-------------");

		velocity = new CashEntry();
		velocity.setDescription("LAST YEAR RETAINED ");
		velocity.setAmount(calculateYearlyRetained(year - 1));
		velocities.add(velocity);
		
		System.out.println("11-------------");

		velocity = new CashEntry();
		velocity.setDescription("ANNUAL RET'D AVERAGE");
		velocity.setAmount(calculateAnnuallyRetainedAverage()+ cb/(year-2014));
		velocities.add(velocity);
		
		System.out.println("12-------------");

		velocity = new CashEntry();
		velocity.setDescription("THIS YEAR VELOCITY");
		double thisYear = calculateYearlyVelocity(year) + AMClient.getCoinbaseBalance().getAmount();
		velocity.setAmount(thisYear);
		velocities.add(velocity);
		
		System.out.println("13-------------");

		velocity = new CashEntry();
		velocity.setDescription("LAST YEAR VELOCITY");
		double lastYear = calculateYearlyVelocity(year - 1);
		velocity.setAmount(lastYear);
		velocities.add(velocity);
		
		System.out.println("14-------------");

		velocity = new CashEntry();
		velocity.setDescription("ANNUAL VELOCITY");
		velocity.setAmount(calculateYearlyVelocity() + cb/(year-2014));
		velocities.add(velocity);
		
		System.out.println("15-------------");
		
		

		CashEntry entry = new CashEntry();
		annuallySpentGained = new ArrayList<CashEntry>();
		for (int i = year; i >= year -3; i--) {
			entry = new CashEntry();
			entry.setAmount(calculateYearlyGained(i));
			entry.setDescription(i +  " GAINED");
			annuallySpentGained.add(entry);

			entry = new CashEntry();
			entry.setAmount(calculateYearlySpent(i));
			entry.setDescription(i +  " SPENT");
			annuallySpentGained.add(entry);
		}

		System.out.println("16--------------");
		
		calculateEntertainmentGoal();
		calculateVacationGoal();
		calculateFoodGoal();
		calculateSnacksGoal();
		calculateHorseCareGoal();
		calculateCarMaintenanceGoal();
		calculateEstimateGoal();
		//calculateDailyVelocity();
		// findUncategoriziedCash();
		
		System.out.println("17-------------");

	
	}

	
	

	public HorizontalBarChartModel getSumByMonthByCategoryChart() {
		return sumByMonthByCategoryChart;
	}

	public void setSumByMonthByCategoryChart(HorizontalBarChartModel sumByMonthByCategoryChart) {
		this.sumByMonthByCategoryChart = sumByMonthByCategoryChart;
	}

	private void createSumByMonthByCategoryChart() {
		sumByMonthByCategoryChart = new HorizontalBarChartModel();

		ChartSeries a = new ChartSeries();
		a.setLabel("CURRENT MONTH");
		ChartSeries b = new ChartSeries();
		b.setLabel("LAST MONTH");
		ChartSeries c = new ChartSeries();
		c.setLabel("LAST YEAR SAME MONTH");
		ChartSeries d = new ChartSeries();
		d.setLabel("AVERAGE");
		sumByMonthByCategoryChart.setSeriesColors("FE2E64,045FB4,58FAAC,FF8000");

		for (CashEntry cash : result) {
			if (cash.getDescription().equalsIgnoreCase("CURRENT MONTH"))
				a.set(cash.getCategoryEntry().getName(), cash.getAmount());
			if (cash.getDescription().equalsIgnoreCase("LAST MONTH"))
				b.set(cash.getCategoryEntry().getName(), cash.getAmount());
			if (cash.getDescription().equalsIgnoreCase("LAST YEAR SAME MONTH"))
				c.set(cash.getCategoryEntry().getName(), cash.getAmount());
			if (cash.getDescription().equalsIgnoreCase("AVERAGE"))
				d.set(cash.getCategoryEntry().getName(), cash.getAmount());
		}
		sumByMonthByCategoryChart.addSeries(b);
		sumByMonthByCategoryChart.addSeries(a);
		sumByMonthByCategoryChart.addSeries(c);
		sumByMonthByCategoryChart.addSeries(d);
		Axis xAxis = sumByMonthByCategoryChart.getAxis(AxisType.X);
		xAxis.setLabel("Amount");
		xAxis.setMin(0);

		Axis yAxis = sumByMonthByCategoryChart.getAxis(AxisType.Y);
		yAxis.setLabel("Categories");
		sumByMonthByCategoryChart.setLegendPosition("se");
	}

	public LineChartModel getTrendsChart() {
		return trendsChart;
	}

	public void setTrendsChart(LineChartModel trendsChart) {
		this.trendsChart = trendsChart;
	}

	public List<CashEntry> getEntries() {
		return entries;
	}

	public void setEntries(List<CashEntry> entries) {
		this.entries = entries;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public double getWfc() {
		return wfc;
	}

	public void setWfc(double wfc) {
		this.wfc = wfc;
	}

	public double getWfsa() {
		return wfsa;
	}

	public void setWfsa(double wfsa) {
		this.wfsa = wfsa;
	}

	public double getWfsb() {
		return wfsb;
	}

	public void setWfsb(double wfsb) {
		this.wfsb = wfsb;
	}

	public double getWfcca() {
		return wfcca;
	}

	public void setWfcca(double wfcca) {
		this.wfcca = wfcca;
	}

	public double getWfccb() {
		return wfccb;
	}

	public void setWfccb(double wfccb) {
		this.wfccb = wfccb;
	}

	public double getCc() {
		return cc;
	}

	public void setCc(double cc) {
		this.cc = cc;
	}

	public double getThisYearsVelocity() {
		return thisYearsVelocity;
	}

	public void setThisYearsVelocity(double thisYearsVelocity) {
		this.thisYearsVelocity = thisYearsVelocity;
	}

	public double getLastYearsVelocity() {
		return lastYearsVelocity;
	}

	public void setLastYearsVelocity(double lastYearsVelocity) {
		this.lastYearsVelocity = lastYearsVelocity;
	}

	public double getMonthlyVelocity() {
		return monthlyVelocity;
	}

	public void setMonthlyVelocity(double monthlyVelocity) {
		this.monthlyVelocity = monthlyVelocity;
	}

	public double getThisMonthsVelocity() {
		return thisMonthsVelocity;
	}

	public void setThisMonthsVelocity(double thisMonthsVelocity) {
		this.thisMonthsVelocity = thisMonthsVelocity;
	}

	public double getLastMonthsVelocity() {
		return lastMonthsVelocity;
	}

	public void setLastMonthsVelocity(double lastMonthsVelocity) {
		this.lastMonthsVelocity = lastMonthsVelocity;
	}

	public List<CashEntry> getTop10Gains() {
		return top10Gains;
	}

	public void setTop10Gains(List<CashEntry> top10Gains) {
		this.top10Gains = top10Gains;
	}

	public List<CashEntry> getTop10Losses() {
		return top10Losses;
	}

	public void setTop10Losses(List<CashEntry> top10Losses) {
		this.top10Losses = top10Losses;
	}

	public List<CashEntry> getAccountSums() {
		return accountSums;
	}

	public void setAccountSums(List<CashEntry> accountSums) {
		this.accountSums = accountSums;
	}

	public List<CashEntry> getVelocities() {
		return velocities;
	}

	public void setVelocities(List<CashEntry> velocities) {
		this.velocities = velocities;
	}

	public double getCs() {
		return cs;
	}

	public void setCs(double cs) {
		this.cs = cs;
	}

	public double getGrandTotal() {
		return grandTotal;
	}

	public void setGrandTotal(double grandTotal) {
		this.grandTotal = grandTotal;
	}

	public List<CashEntry> getAnnuallySpentGained() {
		return annuallySpentGained;
	}

	public void setAnnuallySpentGained(List<CashEntry> annuallySpentGained) {
		this.annuallySpentGained = annuallySpentGained;
	}

	public String getVelocityIndicator() {
		return velocityIndicator;
	}

	public void setVelocityIndicator(String velocityIndicator) {
		this.velocityIndicator = velocityIndicator;
	}

	public String getEntertainmentIndicator() {
		return entertainmentIndicator;
	}

	public void setEntertainmentIndicator(String entertainmentIndicator) {
		this.entertainmentIndicator = entertainmentIndicator;
	}

	public double getMonthlyVelocityGoal() {
		return monthlyVelocityGoal;
	}

	public void setMonthlyVelocityGoal(double monthlyVelocityGoal) {
		this.monthlyVelocityGoal = monthlyVelocityGoal;
	}

	public double getMonthlyVelocityGoalDiff() {
		return monthlyVelocityGoalDiff;
	}

	public void setMonthlyVelocityGoalDiff(double monthlyVelocityGoalDiff) {
		this.monthlyVelocityGoalDiff = monthlyVelocityGoalDiff;
	}

	public double getEntertainmentGoal() {
		return entertainmentGoal;
	}

	public void setEntertainmentGoal(double entertainmentGoal) {
		this.entertainmentGoal = entertainmentGoal;
	}

	public double getEntertainmentGoalDiff() {
		return entertainmentGoalDiff;
	}

	public void setEntertainmentGoalDiff(double entertainmentGoalDiff) {
		this.entertainmentGoalDiff = entertainmentGoalDiff;
	}

	public String getFoodIndicator() {
		return foodIndicator;
	}

	public void setFoodIndicator(String foodIndicator) {
		this.foodIndicator = foodIndicator;
	}

	public String getVacationIndicator() {
		return vacationIndicator;
	}

	public void setVacationIndicator(String vacationIndicator) {
		this.vacationIndicator = vacationIndicator;
	}

	public String getSnacksIndicator() {
		return snacksIndicator;
	}

	public void setSnacksIndicator(String snacksIndicator) {
		this.snacksIndicator = snacksIndicator;
	}

	public double getFoodGoal() {
		return foodGoal;
	}

	public void setFoodGoal(double foodGoal) {
		this.foodGoal = foodGoal;
	}

	public double getFoodGoalDiff() {
		return foodGoalDiff;
	}

	public void setFoodGoalDiff(double foodGoalDiff) {
		this.foodGoalDiff = foodGoalDiff;
	}

	public double getSnacksGoal() {
		return snacksGoal;
	}

	public void setSnacksGoal(double snacksGoal) {
		this.snacksGoal = snacksGoal;
	}

	public double getSnacksGoalDiff() {
		return snacksGoalDiff;
	}

	public void setSnacksGoalDiff(double snacksGoalDiff) {
		this.snacksGoalDiff = snacksGoalDiff;
	}

	public double getVacationGoal() {
		return vacationGoal;
	}

	public void setVacationGoal(double vacationGoal) {
		this.vacationGoal = vacationGoal;
	}

	public double getVacationGoalDiff() {
		return vacationGoalDiff;
	}

	public void setVacationGoalDiff(double vacationGoalDiff) {
		this.vacationGoalDiff = vacationGoalDiff;
	}

	public String getHorseCareIndicator() {
		return horseCareIndicator;
	}

	public void setHorseCareIndicator(String horseCareIndicator) {
		this.horseCareIndicator = horseCareIndicator;
	}

	public String getCarMaintenanceIndicator() {
		return carMaintenanceIndicator;
	}

	public void setCarMaintenanceIndicator(String carMaintenanceIndicator) {
		this.carMaintenanceIndicator = carMaintenanceIndicator;
	}

	public double getHorseCareGoal() {
		return horseCareGoal;
	}

	public void setHorseCareGoal(double horseCareGoal) {
		this.horseCareGoal = horseCareGoal;
	}

	public double getHorseCareGoalDiff() {
		return horseCareGoalDiff;
	}

	public void setHorseCareGoalDiff(double horseCareGoalDiff) {
		this.horseCareGoalDiff = horseCareGoalDiff;
	}

	public double getCarMaintenanceGoal() {
		return carMaintenanceGoal;
	}

	public void setCarMaintenanceGoal(double carMaintenanceGoal) {
		this.carMaintenanceGoal = carMaintenanceGoal;
	}

	public double getCarMaintenanceGoalDiff() {
		return carMaintenanceGoalDiff;
	}

	public void setCarMaintenanceGoalDiff(double carMaintenanceGoalDiff) {
		this.carMaintenanceGoalDiff = carMaintenanceGoalDiff;
	}

	public String getEstimateIndicator() {
		return estimateIndicator;
	}

	public void setEstimateIndicator(String estimateIndicator) {
		this.estimateIndicator = estimateIndicator;
	}

	public String getDailyIndicator() {
		return dailyIndicator;
	}

	public void setDailyIndicator(String dailyIndicator) {
		this.dailyIndicator = dailyIndicator;
	}

	public double getEstimateGoalDiff() {
		return estimateGoalDiff;
	}

	public void setEstimateGoalDiff(double estimateGoalDiff) {
		this.estimateGoalDiff = estimateGoalDiff;
	}

	public double getDailyVelocityDiff() {
		return dailyVelocityDiff;
	}

	public void setDailyVelocityDiff(double dailyVelocityDiff) {
		this.dailyVelocityDiff = dailyVelocityDiff;
	}

	private void createTrendsChart() {
		//sumByMonthEntries = client.getSumByMonth();
		//sumByMonthByCategoryEntries = client.getSumByMonthByCategory();
		trendsChart = new LineChartModel();
		trendsChart.setLegendPosition("se");
		Axis ya = trendsChart.getAxis(AxisType.Y);
		ya.setMin(0);
		trendsChart.getAxes().put(AxisType.X, new CategoryAxis(""));
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		double projection = calculateProjectionVelocity();
		ChartSeries trend = new ChartSeries();
		trend.setLabel("Last Year");
		ChartSeries project = new ChartSeries();
		project.setLabel("This Year");
		ChartSeries estimate = new ChartSeries();
		estimate.setLabel("Estimates");
		double co = 0.0;
		double sm = 0.0;
		String month = "";
		String yAxis = "";

		//estimateEntries = client.getEstimateEntries();

		double ys = 0.0;
		for (CashEntry e : sumByMonthEntries)
			if (Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate())) <= year - 1)
				ys += e.getAmount();

		double pys = 0.0;
		for (CashEntry e : sumByMonthEntries)
			if (Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate())) <= year - 2)
				pys += e.getAmount();

		yAxis = "Year Start";
		estimate.set(yAxis, ys);
		sm = ys;
		for (CashEntry e : estimateEntries) {
			try {
				String mn = new SimpleDateFormat("MMM")
						.format(new SimpleDateFormat("yyyy/MM/dd").parse(year + "/" + e.getId() + "/01"));
				yAxis = mn;
				sm += e.getAmount();
				estimate.set(yAxis, sm);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		co = 0.0;
		sm = 0.0;
		project.set("Year Start", ys);
		for (CashEntry e : sumByMonthEntries) {
			Date date = e.getActualdate();
			if ((afterDate.before(date)) && (beforeDate.after(date))) {
				sm += co;
				co = 0.0;
				calendar = Calendar.getInstance();
				calendar.setTime(date);
				month = new SimpleDateFormat("MMM").format(calendar.getTime());
				sm += e.getAmount();
				yAxis = month;
				project.set(yAxis, sm);
			} else {
				co += e.getAmount();
			}
		}

		Date beginProjectionDate = null;
		try {
			beginProjectionDate = new SimpleDateFormat("yyyy/MMM/dd").parse(year + "/" + month + "/01");

			int begin = Integer.parseInt(new SimpleDateFormat("MM").format(beginProjectionDate));
			for (int i = begin + 1; i <= 12; i++) {
				String mn = new SimpleDateFormat("MMM")
						.format(new SimpleDateFormat("yyyy/MM/dd").parse(year + "/" + i + "/01"));
				yAxis = mn;
				sm += projection;
				project.set(yAxis, sm);
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		double pco = 0.0;
		double psm = 0.0;
		Date pdate = null;
		try {
			pdate = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/01/01");
		} catch (ParseException e2) {
			e2.printStackTrace();
		}

		trend.set("Year Start", pys);
		for (CashEntry e : sumByMonthEntries) {
			Date date = e.getActualdate();
			if (pdate.after(date)) {
				psm += pco;
				pco = 0.0;
				calendar = Calendar.getInstance();
				calendar.setTime(date);
				month = new SimpleDateFormat("MMM").format(calendar.getTime());
				psm += e.getAmount();
				yAxis = month;
				trend.set(yAxis, psm);
			} else {
				pco += e.getAmount();
			}
		}

		trendsChart.addSeries(project);
		trendsChart.addSeries(trend);
		trendsChart.addSeries(estimate);
	}

	private double calculateProjectionVelocity() {
//		LineChartSeries project = new LineChartSeries();
//		project.setLabel("Previous Year");
//		double projection = 0.0;
//		int lowestMonth = 12;
//		int highestMonth = 0;
//		for (CashEntry e : sumByMonthByCategoryEntries) {
//			if (!e.getCategoryEntry().getName().contains("Carry Over")) {
//				int month = Integer.parseInt(new SimpleDateFormat("MM").format(e.getActualdate()));
//				int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()));
//				if (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == year) {
//					projection += e.getAmount();
//					if (month < lowestMonth)
//						lowestMonth = month;
//					if (month > highestMonth)
//						highestMonth = month;
//				}
//			}
//		}
//		double result = projection / (highestMonth - lowestMonth + 1);
		double result = this.monthlyVelocity;
		monthlyVelocityGoalDiff = this.monthlyVelocity - monthlyVelocityGoal;
		if (monthlyVelocityGoalDiff >= 0)
			velocityIndicator = "up";
		else
			velocityIndicator = "down";
		return result;
	}

	private void calculateEntertainmentGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Entertainment"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		entertainmentGoalDiff = t - entertainmentGoal;
		if (entertainmentGoalDiff >= 0)
			entertainmentIndicator = "up";
		else
			entertainmentIndicator = "down";
	}

	private void calculateVacationGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Vacation"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		vacationGoalDiff = t - vacationGoal;
		if (vacationGoalDiff >= 0)
			vacationIndicator = "up";
		else
			vacationIndicator = "down";
	}

	private void calculateSnacksGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Snacks"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		snacksGoalDiff = t - snacksGoal;
		if (snacksGoalDiff >= 0)
			snacksIndicator = "up";
		else
			snacksIndicator = "down";
	}

	private void calculateFoodGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Food"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		foodGoalDiff = t - foodGoal;
		if (foodGoalDiff >= 0)
			foodIndicator = "up";
		else
			foodIndicator = "down";
	}

	private void calculateHorseCareGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Horse Care"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		horseCareGoalDiff = t - horseCareGoal;
		if (horseCareGoalDiff >= 0)
			horseCareIndicator = "up";
		else
			horseCareIndicator = "down";
	}

	private void calculateCarMaintenanceGoal() {
		double t = 0.00;
		for (CashEntry e : sumByMonthByCategoryEntries)
			if ((e.getCategoryEntry().getName().contains("Car Maintenance"))
					&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date())) == Integer
							.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate()))))
				t += e.getAmount();
		carMaintenanceGoalDiff = t - carMaintenanceGoal;
		if (carMaintenanceGoalDiff >= 0)
			carMaintenanceIndicator = "up";
		else
			carMaintenanceIndicator = "down";
	}

//	private void calculateDailyVelocity() {
//		double t = 0.00;
//		String strToday = new SimpleDateFormat("mmddyyyy").format(new Date());
//		for (CashEntry e : sumByMonthByCategoryEntries)
//			if (strToday == new SimpleDateFormat("mmddyyyy").format(e.getActualdate()))
//				t += e.getAmount();
//		if (t >= 0)
//			dailyIndicator = "up";
//		else
//			dailyIndicator = "down";
//	}

	private double calculateMonthlyRetained(int year,int month) {
		return AMClient.getRetainedByMonth(year,month);
	}

	private double calculateYearlyVelocity(int year) {
		return AMClient.getVelocityByYear(year);
	}

	private double calculateYearlyVelocity() {
		double velocity = 0.0;
		List<String> years = new ArrayList<String>();
		for (CashEntry c : sumByMonthEntries)
			if (!years.contains(new SimpleDateFormat("yyyy").format(c.getActualdate()))) {
				int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getActualdate()));
				velocity += calculateYearlyVelocity(year);
				years.add(new SimpleDateFormat("yyyy").format(c.getActualdate()));
			}
		return velocity / years.size();
	}
	

	private double calculateAnnuallyRetainedAverage() {
		double totalRetained = 0.0;
		List<String> years = new ArrayList<String>();
		for (CashEntry c : sumByMonthEntries)
			if (!years.contains(new SimpleDateFormat("yyyy").format(c.getActualdate()))) {
				years.add(new SimpleDateFormat("yyyy").format(c.getActualdate()));
				totalRetained += calculateYearlyRetained(
						Integer.parseInt(new SimpleDateFormat("yyyy").format(c.getActualdate())));
			}
		return totalRetained / years.size();
	}

	private double calculateYearlyRetained(int year) {
		return AMClient.getRetainedByYear(year);
	}

	private double calculateYearlySpent(int year) {
		return AMClient.getSpentByYear(year);
	}

	private double calculateYearlyGained(int year) {
		return AMClient.getGainedByYear(year);
	}

	private void calculateEstimateGoal() {
		int month = Integer.parseInt(new SimpleDateFormat("MM").format(new Date()));
		int year = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		estimateGoalDiff = calculateMonthlyRetained(year,month);
		for (CashEntry c : estimateEntries)
			if ((month) == c.getId())
				estimateGoalDiff = estimateGoalDiff - c.getAmount();
		if (estimateGoalDiff >= 0)
			estimateIndicator = "up";
		else
			estimateIndicator = "down";
	}

	/*private double findUncategoriziedCash() {
		double result = 0.0;
		List<CategoryEntry> categoryEntries = AMClient.getCategoryEntries();
		boolean found = false;
		for (CashEntry c : cashEntries) {
			found = false;
			for (CategoryEntry e : categoryEntries)
				if (e.getId() == c.getCategoryEntry().getId())
					found = true;
			if (!found)
				System.out.println("CATEGORY " + c.getCategoryEntry().getId() + " NOT FOUND");
		}
		return result;
	}*/

}
