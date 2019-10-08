package client.jobs;

import com.mashape.unirest.http.HttpMethod;
import constant.PhaseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statistic.Record;
import statistic.RecordContainer;

/**
 * @author create by Xiao Han 10/1/19
 * @version 1.0
 * @since jdk 1.8
 */
public class Counter {

  private static Logger logger = LoggerFactory.getLogger(Counter.class);

  private final Object lock;
  private int max;
  private int current;
  private PhaseEnum phaseEnum;

  public Counter(PhaseEnum phaseEnum, Object lock, int max) {
    this.phaseEnum = phaseEnum;
    this.lock = lock;
    this.max = max;
    logger.info(phaseEnum.toString() + " start! Max requests " + max);
  }

  public synchronized void update(int startTime, HttpMethod method, long lantency, int statusCode) {
    current++;
    RecordContainer.enqueue(new Record(startTime, method, lantency, statusCode));
    if (max == current) {
      StatusListener.completePhase();
    }
    double percentage = current * 100.0 / max;
    if (percentage >= 10 && percentage % 10 == 0){
      logger.info(phaseEnum.toString() + " " + percentage + "% completed");
    }
    if (lock == null) return;
    if (percentage >= 10) {
      synchronized (lock) {
        lock.notifyAll();
      }
    }
  }

  public int getMax() {
    return this.max;
  }
}
