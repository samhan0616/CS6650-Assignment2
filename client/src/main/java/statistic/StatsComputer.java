package statistic;

import client.Executor;
import client.ExecutorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statistic.graph.RecordDataGrapher;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.PriorityQueue;

/**
 * @author create by Xiao Han 10/6/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatsComputer {

  private Logger logger = LoggerFactory.getLogger(StatsComputer.class);

  private long total;
  private long startTime;
  private long maxResponseTime;
  private double percentilePos;
  private int totalRequestNumber;
  private RecordDataGrapher grapher;

  static long end = 0;

  private PriorityQueue<Long> percentilePQ;
  private PriorityQueue<Long> medianPQSmall;
  private PriorityQueue<Long> medianPQLarge;

  public StatsComputer(int totalRequestNumber, long startTime) {
    this.totalRequestNumber = totalRequestNumber;
    this.startTime = startTime;
    this.percentilePos = Math.ceil(0.01 * totalRequestNumber);
    this.percentilePQ = new PriorityQueue<>();
    this.medianPQSmall = new PriorityQueue<>();
    this.medianPQLarge = new PriorityQueue<>();
    this.grapher = new RecordDataGrapher(ExecutorContext.numThreads + "_Time_Series");
  }

  public void complete() {
    File file = new File(ExecutorContext.numThreads + "_threads_statistic.txt");
    FileWriter fileWriter;
    try {
      fileWriter = new FileWriter(file);
      fileWriter.write(String.format("Mean response time: %.2f ms" + System.getProperty("line.separator"),(double)total / totalRequestNumber));
      double median = medianPQLarge.size() > medianPQSmall.size()
              ? medianPQLarge.peek()
              : (medianPQLarge.peek() - medianPQSmall.peek()) / 2.0;
      fileWriter.write(String.format("Median response time: %.2f ms" + System.getProperty("line.separator"), median));
      fileWriter.write(String.format("Wall time: %d sec Throughput: %.2f" + System.getProperty("line.separator"),
              (end - startTime) / 1000,  (double)totalRequestNumber * 1000 / (end - startTime)));
      fileWriter.write(String.format("99th percentile: %d ms" + System.getProperty("line.separator"), percentilePQ.peek()));
      fileWriter.write(String.format("Max response time: %d ms" + System.getProperty("line.separator"), maxResponseTime));
      fileWriter.flush();
      fileWriter.close();
      grapher.print();
    } catch (IOException e) {
      e.printStackTrace();
    }
    logger.info("Computing completed, all tasks finished");
  }

  void update(Record record) {
    updateTotal(record.getLatency());
    updatePercentile(record.getLatency());
    updateMeidan(record.getLatency());
    updateMax(record.getLatency());
    updateGraph(record);
  }

  private void updateGraph(Record record) {
    grapher.addData(record);
  }

  private void updateMax(long latency) {
    maxResponseTime = Math.max(maxResponseTime, latency);
  }

  private void updateMeidan(long latency) {
    medianPQLarge.add(latency);
    medianPQSmall.add(-medianPQLarge.poll());
    if (medianPQLarge.size() < medianPQSmall.size())
      medianPQLarge.add(-medianPQSmall.poll());
  }

  private void updatePercentile(long latency) {
    if (percentilePQ.size() < percentilePos) {
      percentilePQ.add(latency);
    } else {
      if (!percentilePQ.isEmpty() && percentilePQ.peek() < latency) {
        percentilePQ.poll();
        percentilePQ.add(latency);
      }
    }
  }

  private void updateTotal(long latency) {
    this.total += latency;
  }


}
