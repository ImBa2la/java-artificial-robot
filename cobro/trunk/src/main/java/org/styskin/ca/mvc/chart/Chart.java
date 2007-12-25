package org.styskin.ca.mvc.chart;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

public class Chart {	
	
	public static JPanel getPanel() {
		XYDataset dataset = createDataset();
	    JFreeChart chart = createChart(dataset);
	    ChartPanel chartPanel = new ChartPanel(chart, false);
	    chartPanel.setPreferredSize(new Dimension(500, 270));
	    return chartPanel;
	}

	private static XYDataset createDataset() {
		DefaultXYDataset dataset = new DefaultXYDataset();
		dataset.addSeries("Test", new double[][] {{0.1, 0.2, 0.5},  {0.25, 0.5, 0.7}});
		return dataset;
	}

	private static JFreeChart createChart(XYDataset dataset) {

		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart("Demo", "x", "y", dataset,
				PlotOrientation.VERTICAL
				, true, true, false);
		
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

		// set the background color for the chart...
		chart.setBackgroundPaint(Color.white);

		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setDomainGridlinesVisible(true);
		plot.setRangeGridlinePaint(Color.white);

		// ******************************************************************
		//  More than 150 demo applications are included with the JFreeChart
		//  Developer Guide...for more information, see:
		//
		//  >   http://www.object-refinery.com/jfreechart/guide.html
		//
		// ******************************************************************

		// set the range axis to display integers only...
		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		return chart;

	}

}
