package org.styskin.ca.mvc.chart;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.styskin.ca.math.Function;

public class ComplexGraphPanel {
	
	private DefaultXYDataset dataset;
	private JFreeChart chart;
	private ChartPanel panel;
	
	
	public ComplexGraphPanel() {
		dataset = new DefaultXYDataset();
		chart = createChart(dataset);
	    panel = new ChartPanel(chart, false);
	    panel.setPreferredSize(new Dimension(500, 270));
	}
	
	public JPanel getPanel() {
		return panel;
	}
	
	@SuppressWarnings("unchecked")
	public void removeAllSeries() {
		List<Comparable> names = new ArrayList<Comparable>();
		for(int i=0; i < dataset.getSeriesCount(); ++i) {
			names.add(dataset.getSeriesKey(i));
		}
		for(int i=0; i < names.size(); ++i) {
			dataset.removeSeries(names.get(i));
		}
	}

	public void updateChart(String name, Function func) {
		updateChart(name, func, 0, 1);
	}
	
	public void updateChart(String name, Function func, double xMin, double xMax) {
		int n = 100;
		double[] x = new double[n];
		double[] f = new double[n];
		double step = (xMax - xMin) / n; 
		x[0] = xMin;
		for(int i=1; i < x.length; i ++) {
			x[i] = x[i-1] + step;
			f[i] = func.getValue(x[i]);
		}
		dataset.addSeries(name, new double[][] {x, f});
	}
		

	private JFreeChart createChart(XYDataset dataset) {
		JFreeChart chart = ChartFactory.createXYLineChart("Demo", "x", "y", dataset,
				PlotOrientation.VERTICAL
				, true, true, false);
		
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;
	}

}
