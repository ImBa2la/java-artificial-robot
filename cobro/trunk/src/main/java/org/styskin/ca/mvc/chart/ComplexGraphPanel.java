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
import org.styskin.ca.functions.complex.ComplexOperator;

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
	
	public void updateChart(ComplexOperator op) {
		dataset.removeSeries("phi");		
		dataset.removeSeries("ksi");
		double[] x = new double[100];
		double[] p = new double[100];
		double[] k = new double[100];
		for(int i=1; i < x.length; i ++) {
			x[i] = x[i-1] + 1d/x.length;
			p[i] = op.getPhi(x[i]);
			k[i] = op.getKsi(x[i]);
		}
		dataset.addSeries("phi", new double[][] {x, p});
		dataset.addSeries("ksi", new double[][] {x, k});
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
