package NeuralNetwork.NN;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.JFrame;

import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class LinePlotter extends JFrame{
    private static ArrayList<Double> cost = new ArrayList<>();
    private static int showCost;
    public LinePlotter(String chartTitle, ArrayList<Double> cost, int showCost){
        this.cost = new ArrayList<>(cost);
        this.showCost = showCost;
        JFreeChart lineChart = ChartFactory.createLineChart(
                chartTitle,
                "Number of iterations", "Cost",
                createDataset(),
                PlotOrientation.VERTICAL,
                true,true,false);

        ChartPanel chartPanel = new ChartPanel(lineChart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500,400));
        CategoryPlot categoryPlot = lineChart.getCategoryPlot();
        CategoryItemRenderer cir = categoryPlot.getRenderer();
        // change line color
        cir.setSeriesPaint(0, new Color(175, 20, 35));
        // change thickness
        cir.setSeriesStroke(0,new BasicStroke(2.0f));

        categoryPlot.setBackgroundPaint(Color.white);
        categoryPlot.setRangeGridlinesVisible(true);
        categoryPlot.setDomainGridlinesVisible(true);
        categoryPlot.setRangeGridlinePaint(Color.black);
        categoryPlot.setDomainGridlinePaint(Color.black);
        lineChart.getLegend().setFrame(BlockBorder.NONE);
        setContentPane(chartPanel);
    }


    public static DefaultCategoryDataset createDataset(){
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        int i=0;
        for(Double num: cost){
            dataset.addValue(num, "cost", "" + (i*showCost));
            i++;
        }
        return dataset;
    }

    public static void plotCost(String title, ArrayList<Double> cost, int showCost){
        LinePlotter chart = new LinePlotter(title, cost, showCost);
        chart.pack();
        chart.setVisible(true);
    }

}
