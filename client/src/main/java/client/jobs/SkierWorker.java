package client.jobs;

import client.ExecutorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author create by Xiao Han 10/1/19
 * @version 1.0
 * @since jdk 1.8
 */
public class SkierWorker {

  private static Logger logger = LoggerFactory.getLogger(SkierWorker.class);

  private ExecutorService service;
  private int threads;
  private int timeStart;
  private int timeEnd;
  private Counter counter;

  public SkierWorker(int threads, int timeStart, int timeEnd, Counter counter) {
    this.threads = threads;
    this.service = Executors.newFixedThreadPool(threads);
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.counter = counter;
  }

  public void run() {
    int requestPerThread = counter.getMax() / threads;
    int skierIDGap = ExecutorContext.numSkiers / threads;
    for (int i = 0; i < threads; i++) {
      service.execute(new SkierThread(i, requestPerThread, skierIDGap, timeStart, timeEnd, counter));
    }
    service.shutdown();
  }
}
