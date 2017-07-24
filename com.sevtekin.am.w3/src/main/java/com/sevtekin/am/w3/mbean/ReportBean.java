package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

import org.primefaces.event.RowEditEvent;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;
import com.sevtekin.am.common.CategoryEntry;

public class ReportBean implements Serializable {

	private static final long serialVersionUID = 1L;
	AMClient client = new AMClient();
	List<CashEntry> sumByMonthEntries = null;
	List<CashEntry> sumByMonthByCategoryEntries = null;
	List<CashEntry> estimateEntries = null;
	private BarChartModel sumByMonthChart;
	private HorizontalBarChartModel sumByMonthByCategoryChart;
	private LineChartModel trendChart;
	private Date beforeDate = null;
	private Date afterDate = null;
	private String cyear = "";
	private int year;

	public ReportBean() {
		estimateEntries = client.getEstimateEntries();
		sumByMonthEntries = client.getCashEntriesSumByMonth();
		Date today = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
		cyear = new SimpleDateFormat("yy").format(calendar.getTime());
		beforeDate = calendar.getTime();
		year = calendar.get(Calendar.YEAR);
		try {
			beforeDate = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/12/31");
			afterDate = new SimpleDateFormat("yyyy/MM/dd").parse(year + "/01/01");
			createCharts();
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
	}

	@PostConstruct
	public void initialize() {

	}

	public List<CashEntry> getSumByMonthEntries() {
		return sumByMonthEntries;
	}

	public void setSumByMonthEntries(List<CashEntry> sumByMonthEntries) {
		this.sumByMonthEntries = sumByMonthEntries;
	}

	public List<CashEntry> getSumByMonthByCategoryEntries() {
		return sumByMonthByCategoryEntries;
	}

	public void setSumByMonthByCategoryEntries(List<CashEntry> sumByMonthByCategoryEntries) {
		this.sumByMonthByCategoryEntries = sumByMonthByCategoryEntries;
	}

	public BarChartModel getSumByMonthChart() {
		return sumByMonthChart;
	}

	public void setSumByMonthChart(BarChartModel sumByMonthChart) {
		this.sumByMonthChart = sumByMonthChart;
	}

	public LineChartModel getTrendChart() {
		return trendChart;
	}

	public void setTrendChart(LineChartModel trendChart) {
		this.trendChart = trendChart;
	}

	public BarChartModel getSumByMonthByCategoryChart() {
		return sumByMonthByCategoryChart;
	}

	public void setSumByMonthByCategoryChart(HorizontalBarChartModel sumByMonthByCategoryChart) {
		this.sumByMonthByCategoryChart = sumByMonthByCategoryChart;
	}

	public Date getBeforeDate() {
		return beforeDate;
	}

	public void setBeforeDate(Date beforeDate) {
		this.beforeDate = beforeDate;
	}

	public Date getAfterDate() {
		return afterDate;
	}

	public void setAfterDate(Date afterDate) {
		this.afterDate = afterDate;
	}

	public List<CashEntry> getEstimateEntries() {
		return estimateEntries;
	}

	public void setEstimateEntries(List<CashEntry> estimateEntries) {
		this.estimateEntries = estimateEntries;
	}

	public void createCharts() {
		sumByMonthEntries = client.getCashEntriesSumByMonth();
		sumByMonthByCategoryEntries = client.getCashEntriesSumByMonthByCategory();
		createSumByMonthChart();
		createTrendChart();
		createSumByMonthByCategoryChart();
	}

	private void createSumByMonthChart() {
		sumByMonthChart = new BarChartModel();
		sumByMonthChart.setStacked(false);
		sumByMonthChart.setLegendPosition("ne");
		ChartSeries sums = new ChartSeries();
		sums.setLabel("Current Range");
		ChartSeries pSums = new ChartSeries();
		pSums.setLabel("Last Year");
		ChartSeries eSums = new ChartSeries();
		eSums.setLabel("Estimates");

		double ys = 0.0;
		if (year == 2015) {
			for (CashEntry e : sumByMonthByCategoryEntries)
				if ((e.getCategoryEntry().getName().contains("Carry Over")))
					ys += e.getAmount();
		} else {
			for (CashEntry e : sumByMonthByCategoryEntries)
				if ((!e.getCategoryEntry().getName().contains("Carry Over")
						&& (Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate())) == year - 1)))
					ys += e.getAmount();
		}

		for (CashEntry e : sumByMonthEntries) {
			Date date = e.getActualdate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String month = new SimpleDateFormat("MMM").format(calendar.getTime());
			int mnt = Integer.parseInt(new SimpleDateFormat("M").format(calendar.getTime()));
			String year = new SimpleDateFormat("yy").format(calendar.getTime());
			String yAxis = month;
			Double amount = 0.0;
			Double estimate = 0.0;
			if ((afterDate.before(date)) && (beforeDate.after(date))) {
				if (year.equalsIgnoreCase(cyear)) {
					amount = 0.0;
					estimate = 0.0;
					for (CashEntry ee : sumByMonthEntries) {
						Date ddate = ee.getActualdate();
						Calendar ccalendar = Calendar.getInstance();
						ccalendar.setTime(ddate);
						String pmonth = new SimpleDateFormat("MMM").format(ccalendar.getTime());
						String pyear = new SimpleDateFormat("yy").format(ccalendar.getTime());
						if ((pmonth.equalsIgnoreCase(month))
								&& (Integer.parseInt(pyear) + 1 == Integer.parseInt(cyear)))
							amount = ee.getAmount();
					}
					for (CashEntry ee : estimateEntries)
						if (ee.getId() == mnt)
							estimate = ee.getAmount();
				}
				sums.set(yAxis, e.getAmount());
				pSums.set(yAxis, amount);
				eSums.set(yAxis, estimate);
			}
		}
		pSums.set("Year Start", ys);
		sumByMonthChart.addSeries(sums);
		sumByMonthChart.addSeries(pSums);
		sumByMonthChart.addSeries(eSums);
	}

	private void createTrendChart() {
		trendChart = new LineChartModel();
		Axis ya = trendChart.getAxis(AxisType.Y);
		ya.setMin(0);
		trendChart.getAxes().put(AxisType.X, new CategoryAxis(""));
		trendChart.setLegendPosition("se");
		ChartSeries trend = new ChartSeries();
		trend.setLabel("Current Range");
		ChartSeries ptrend = new ChartSeries();
		ptrend.setLabel("Last Year");
		ChartSeries etrend = new ChartSeries();
		etrend.setLabel("Estimates");
		double co = 0.0;
		double sm = 0.0;
		//double lastYearEnd = 0.0;
		double psm = 0.0;
		//double previousLastYearEnd = 0.0;
		double esm = 0.0;
		
		int cy = Integer.parseInt(new SimpleDateFormat("yyyy").format(new Date()));
		for (CashEntry e : sumByMonthEntries)
			if (Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate())) <= cy-1)
				esm += e.getAmount();
		for (CashEntry e : sumByMonthEntries)
			if (Integer.parseInt(new SimpleDateFormat("yyyy").format(e.getActualdate())) <= cy-2)
				psm+=e.getAmount();
		System.out.println("CYEAR " + cy + " PSM " + psm + " ESM " + esm);
		
		for (CashEntry e : sumByMonthEntries) {
			Date date = e.getActualdate();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			String month = new SimpleDateFormat("MMM").format(calendar.getTime());
			int mnt = Integer.parseInt(new SimpleDateFormat("M").format(calendar.getTime()));
			String year = new SimpleDateFormat("yy").format(calendar.getTime());
			String yAxis = month;
			if ((afterDate.before(date)) && (beforeDate.after(date))) {
				if (year.equalsIgnoreCase(cyear)) {
					for (CashEntry ee : sumByMonthEntries) {
						Date ddate = ee.getActualdate();
						Calendar ccalendar = Calendar.getInstance();
						ccalendar.setTime(ddate);
						String pmonth = new SimpleDateFormat("MMM").format(ccalendar.getTime());
						String pyear = new SimpleDateFormat("yy").format(ccalendar.getTime());
						if ((pmonth.equalsIgnoreCase(month))
								&& (Integer.parseInt(pyear) + 1 == Integer.parseInt(cyear))) {
							psm += ee.getAmount();
							ptrend.set(yAxis, psm);
						}
					}
					for (CashEntry eee : estimateEntries) {
						if (eee.getId() == mnt) {
							esm += eee.getAmount();
							etrend.set(yAxis, esm);
						}
					}
				}
				sm += co;
				co = 0.0;
				sm += e.getAmount();
				trend.set(yAxis, sm);

			} else {
				co += e.getAmount();
			}
		}
		trendChart.addSeries(trend);
		trendChart.addSeries(ptrend);
		trendChart.addSeries(etrend);
	}

	private void createSumByMonthByCategoryChart() {
		sumByMonthByCategoryChart = new HorizontalBarChartModel();
		sumByMonthByCategoryChart.setLegendPosition("se");
		ChartSeries chart = new ChartSeries();
		chart.setLabel("This Year");
		ChartSeries pchart = new ChartSeries();
		pchart.setLabel("Last Year");
		Map<String, Double> map = new HashMap<String, Double>();
		Map<String, Double> pmap = new HashMap<String, Double>();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(afterDate);
		calendar.add(Calendar.YEAR, -1);
		Date pAfterDate = calendar.getTime();
		calendar.setTime(new Date());
		calendar.add(Calendar.YEAR, -1);
		Date pBeforeDate = calendar.getTime();

		for (CashEntry e : sumByMonthByCategoryEntries) {
			if ((afterDate.before(e.getActualdate())) && (beforeDate.after(e.getActualdate()))) {
				if (!map.containsKey(e.getCategoryEntry().getName())) {
					map.put(e.getCategoryEntry().getName(), e.getAmount());
				} else {
					map.put(e.getCategoryEntry().getName(), map.get(e.getCategoryEntry().getName()) + e.getAmount());
				}
			} else if ((pAfterDate.before(e.getActualdate())) && (pBeforeDate.after(e.getActualdate()))) {
				if (!pmap.containsKey(e.getCategoryEntry().getName())) {
					pmap.put(e.getCategoryEntry().getName(), e.getAmount());
				} else {
					pmap.put(e.getCategoryEntry().getName(), pmap.get(e.getCategoryEntry().getName()) + e.getAmount());
				}
			}
		}

		List<CategoryEntry> cats = client.getCategoryEntries();
		for (CategoryEntry cat : cats) {
			if (!map.containsKey(cat.getName()))
				map.put(cat.getName(), 0.0);
			if (!pmap.containsKey(cat.getName()))
				pmap.put(cat.getName(), 0.0);
		}

		Map<String, Double> tp1 = new TreeMap<String, Double>(map);
		Map<String, Double> tp2 = new TreeMap<String, Double>(pmap);

		Iterator<Entry<String, Double>> iterator = tp1.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Double> entry = iterator.next();
			chart.set(entry.getKey(), (double) entry.getValue());
		}
		iterator = tp2.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<String, Double> entry = iterator.next();
			pchart.set(entry.getKey(), (double) entry.getValue());
		}
		sumByMonthByCategoryChart.addSeries(chart);
		sumByMonthByCategoryChart.addSeries(pchart);
	}

	public void saveEstimateEntry(RowEditEvent event) {
		try {
			CashEntry entry = (CashEntry) event.getObject();
			System.out.println(new Timestamp(new Date().getTime()) + " [AM W3][INFO] Save requested for "
					+ entry.getId() + " - " + entry.getAmount());
			client.updateEstimateEntry(entry);
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"ESTIMATE SAVED", entry.getId() + " - " + entry.getAmount()));
		} catch (Exception e) {
			System.out.println(new Timestamp(new Date().getTime()) + " [AM W3] [ERROR] " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "ERROR", e.toString()));
		}
	}
}
