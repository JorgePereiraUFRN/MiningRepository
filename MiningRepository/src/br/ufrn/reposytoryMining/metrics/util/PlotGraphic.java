package br.ufrn.reposytoryMining.metrics.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;



public class PlotGraphic extends ApplicationFrame {

	private String title;
	private String metric;
	private double idealValue;
	private Map<String, Double> values = new HashMap<>();

	public PlotGraphic(String title, String metric, double idealValue,
			Map<String, Double> values) {
		super(title);
		this.title = title;
		this.metric = metric;
		this.idealValue = idealValue;
		this.values = values;

	}

	public void plot() {
		
		final CategoryDataset dataset = createDataset();
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new Dimension(500, 270));
		setContentPane(chartPanel);
		
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);

	}

	private CategoryDataset createDataset() {

		final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		if (values != null) {

		String[] colums = values.keySet().toArray(new String[values.size()]);
		
		Arrays.sort(colums);
		
			for (String version : colums) {

				dataset.addValue(values.get(version), metric, version);
				dataset.addValue(idealValue, "Ideal", version);

			}

		}

		return dataset;
	}

	private JFreeChart createChart(final CategoryDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createLineChart(title, // chart
																		// title
				"Metric: " + metric, // domain axis label
				"Value", // range axis label
				dataset, // data
				PlotOrientation.VERTICAL, // orientation
				true, // include legend
				true, // tooltips
				false // urls
				);

		chart.setBackgroundPaint(Color.white);

		final CategoryPlot plot = (CategoryPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setRangeGridlinePaint(Color.white);

		// customise the range axis...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		rangeAxis.setAutoRangeIncludesZero(true);

		// ****************************************************************************
		// * JFREECHART DEVELOPER GUIDE *
		// * The JFreeChart Developer Guide, written by David Gilbert, is
		// available *
		// * to purchase from Object Refinery Limited: *
		// * *
		// * http://www.object-refinery.com/jfreechart/guide.html *
		// * *
		// * Sales are used to provide funding for the JFreeChart project -
		// please *
		// * support us so that we can continue developing free software. *
		// ****************************************************************************

		// customise the renderer...
		final LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot
				.getRenderer();
		// renderer.setDrawShapes(true);

		renderer.setSeriesStroke(0, new BasicStroke(2.0f,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 10.0f, 6.0f }, 0.0f));
		renderer.setSeriesStroke(1, new BasicStroke(2.0f,
				BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f,
				new float[] { 6.0f, 6.0f }, 0.0f));
		/*
		 * renderer.setSeriesStroke( 2, new BasicStroke( 2.0f,
		 * BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 1.0f, new float[]
		 * {9.0f, 9.0f}, 9.0f ) );
		 */
		// OPTIONAL CUSTOMISATION COMPLETED.

		return chart;
	}

	public static void main(final String[] args) {

		Map<String, Double> values = new HashMap<>();

		values.put("1", 1.0);
		values.put("2", 8.0);
		values.put("3", 7.0);
		values.put("4", 3.0);
		values.put("5", 13.0);
		values.put("5", 4.0);

		final PlotGraphic g = new PlotGraphic("CA Metrcis", "CA", 5, values);
		
		g.plot();
	}

}
