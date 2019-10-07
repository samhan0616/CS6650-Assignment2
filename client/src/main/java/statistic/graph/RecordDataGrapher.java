package statistic.graph;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import statistic.Record;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

/**
 * @author create by Xiao Han 10/6/19
 * @version 1.0
 * @since jdk 1.8
 */
public class RecordDataGrapher {
  public static final int WEIGHT = 1200;
  public static final int HEIGHT = 800;
  private TimeSeries timeseries ;
  private String name;

  public RecordDataGrapher(String name) {
    this.timeseries = new TimeSeries("time");
    this.name = name;
  }

  public void addData(Record record) {
    timeseries.addOrUpdate(new Millisecond(new Date(record.getTimeStamp())), record.getLatency());
  }

  public void print() {
    JFreeChart jfreechart = ChartFactory.createTimeSeriesChart("Time VS Latency", "Time", "Latency",
            getDataSet());
    File outFile = new File(name +".png");
    try (FileOutputStream out = new FileOutputStream(outFile)) {
      ChartUtilities.writeChartAsPNG(out, jfreechart, WEIGHT, HEIGHT);
      out.flush();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }


  }

  private XYDataset getDataSet(){
    TimeSeriesCollection timeseriescollection = new TimeSeriesCollection();
    timeseriescollection.addSeries(timeseries);
    return timeseriescollection;
  }

}
