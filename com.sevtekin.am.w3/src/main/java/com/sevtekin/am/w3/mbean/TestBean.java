package com.sevtekin.am.w3.mbean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.CategoryAxis;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

import com.sevtekin.am.client.AMClient;
import com.sevtekin.am.common.CashEntry;

@ManagedBean
public class TestBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private LineChartModel lineModel1;
	List<CashEntry> sumByMonthEntries = null;
	List<CashEntry> sumByMonthByCategoryEntries = null;
	List<CashEntry> estimateEntries = null;
	private Date beforeDate = null;
	private Date afterDate = null;
	AMClient client = new AMClient();
	private HorizontalBarChartModel horizontalBarModel;

	@PostConstruct
	public void init() {
		// sumByMonthEntries = client.getCashEntriesSumByMonth();
		// sumByMonthByCategoryEntries = client
		// .getCashEntriesSumByMonthByCategory();
		// Date today = new Date();
		// Calendar calendar = Calendar.getInstance();
		// calendar.setTime(today);
		// beforeDate = calendar.getTime();
		// int year = calendar.get(Calendar.YEAR);
		// try {
		// beforeDate = new SimpleDateFormat("yyyy/MM/dd").parse(year
		// + "/12/31");
		// afterDate = beforeDate;
		// for (CashEntry e : sumByMonthEntries)
		// if (e.getActualdate().before(afterDate))
		// afterDate = e.getActualdate();
		// String mnth = new SimpleDateFormat("MM").format(afterDate);
		// String yr = new SimpleDateFormat("yy").format(afterDate);
		// afterDate = new SimpleDateFormat("yyyy/MM/dd").parse(yr + "/"
		// + mnth + "/1");
		// } catch (ParseException e1) {
		// e1.printStackTrace();
		// }
		// createLineModels();
		createHorizontalBarModel();
	}

	public LineChartModel getLineModel1() {
		return lineModel1;
	}

	public HorizontalBarChartModel getHorizontalBarModel() {
		return horizontalBarModel;
	}

	private void createLineModels() {
		lineModel1 = initLinearModel();
		lineModel1.setTitle("Linear Chart");
		lineModel1.setLegendPosition("e");
		Axis yAxis = lineModel1.getAxis(AxisType.Y);
		yAxis.setMin(0);
		lineModel1.getAxes().put(AxisType.X, new CategoryAxis(""));
	}

	private LineChartModel initLinearModel() {
		LineChartModel model = new LineChartModel();

		sumByMonthEntries = client.getSumByMonth();
		sumByMonthByCategoryEntries = client
				.getSumByMonthByCategory();

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		double projection = calculateMonthlyProjection();
		ChartSeries trend = new ChartSeries();
		trend.setLabel("Cash Trend");
		ChartSeries project = new ChartSeries();
		project.setLabel("Projection");
		ChartSeries estimate = new ChartSeries();
		estimate.setLabel("Estimate");
		double co = 0.0;
		double sm = 0.0;
		String month = "";
		String yAxis = "";
		estimateEntries = client.getEstimateEntries();

		for (CashEntry e : estimateEntries) {
			try {
				String mn = new SimpleDateFormat("MMM")
						.format(new SimpleDateFormat("yyyy/MM/dd").parse(year
								+ "/" + e.getId() + "/01"));
				yAxis = mn + year;
				sm += e.getAmount();
				estimate.set(yAxis, sm);
			} catch (ParseException e1) {
				e1.printStackTrace();
			}
		}
		co = 0.0;
		sm = 0.0;
		for (CashEntry e : sumByMonthEntries) {
			Date date = e.getActualdate();
			if ((afterDate.before(date)) && (beforeDate.after(date))) {
				sm += co;
				co = 0.0;
				calendar = Calendar.getInstance();
				calendar.setTime(date);
				month = new SimpleDateFormat("MMM").format(calendar.getTime());
				sm += e.getAmount();
				yAxis = month + year;
				trend.set(yAxis, sm);
				project.set(yAxis, sm);
				System.out.println("Adding series: " + yAxis + " " + sm);
			} else {
				co += e.getAmount();
			}
		}

		Date beginProjectionDate = null;
		try {
			beginProjectionDate = new SimpleDateFormat("yyyy/MMM/dd")
					.parse(year + "/" + month + "/01");

			int begin = Integer.parseInt(new SimpleDateFormat("MM")
					.format(beginProjectionDate));
			System.out.println("begin " + begin);
			for (int i = begin + 1; i <= 12; i++) {
				String mn = new SimpleDateFormat("MMM")
						.format(new SimpleDateFormat("yyyy/MM/dd").parse(year
								+ "/" + i + "/01"));
				yAxis = mn + year;
				sm += projection;
				project.set(yAxis, sm);
				System.out.println("Adding projection");
			}
		} catch (ParseException e1) {
			e1.printStackTrace();
		}
		model.addSeries(estimate);
		model.addSeries(project);
		model.addSeries(trend);

		// ChartSeries boys = new ChartSeries();
		// boys.setLabel("Boys");
		// boys.set("2004", 12000);
		// boys.set("2005", 10000);
		// boys.set("2006", 44000);
		// boys.set("2007", 15000);
		// boys.set("2009", 25000);
		// model.addSeries(boys);

		return model;
	}

	private double calculateMonthlyProjection() {
		LineChartSeries project = new LineChartSeries();
		project.setLabel("Projection");
		double projection = 0.0;
		int lowestMonth = 12;
		int highestMonth = 0;
		for (CashEntry e : sumByMonthByCategoryEntries) {
			if (!e.getCategoryEntry().getName().contains("Carry Over")) {
				projection += e.getAmount();
				int month = Integer.parseInt(new SimpleDateFormat("MM")
						.format(e.getActualdate()));
				if (month < lowestMonth)
					lowestMonth = month;
				if (month > highestMonth)
					highestMonth = month;
			}
		}
		return projection / (highestMonth - lowestMonth + 1);
	}

	private void createHorizontalBarModel() {
		horizontalBarModel = new HorizontalBarChartModel();

		ChartSeries boys = new ChartSeries();
		boys.setLabel("Boys");
		boys.set("2004", 50);
		boys.set("2005", 96);
		boys.set("2006", 44);
		boys.set("2007", 55);
		boys.set("2008", 25);

		ChartSeries girls = new ChartSeries();
		girls.setLabel("Girls");
		girls.set("2004", 52);
		girls.set("2005", 60);
		girls.set("2006", 82);
		girls.set("2007", 35);
		girls.set("2008", 120);

		horizontalBarModel.addSeries(boys);
		horizontalBarModel.addSeries(girls);

		horizontalBarModel.setTitle("Horizontal and Stacked");
		horizontalBarModel.setLegendPosition("e");
		horizontalBarModel.setStacked(true);

		Axis xAxis = horizontalBarModel.getAxis(AxisType.X);
		xAxis.setLabel("Births");
		xAxis.setMin(0);
		xAxis.setMax(200);

		Axis yAxis = horizontalBarModel.getAxis(AxisType.Y);
		yAxis.setLabel("Gender");
	}

}