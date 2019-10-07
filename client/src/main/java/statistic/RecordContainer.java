package statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class RecordContainer {
  private static Logger logger = LoggerFactory.getLogger(RecordContainer.class);
  private static LinkedBlockingQueue<Record> queue;

  static {
    queue = new LinkedBlockingQueue<>();
  }

  public static void enqueue(Record e) {
    queue.add(e);
  }

  public static Record dequeue() {
    try {
      return queue.poll(1000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
      logger.error("Failed to access blocking queue");
    }
    return null;
  }
}
