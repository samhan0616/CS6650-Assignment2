package client.jobs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statistic.StatsComputer;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatusListener {

  private static Logger logger = LoggerFactory.getLogger(StatusListener.class);

  private static boolean completed;
  public static long start;

  static {
    completed = false;
  }

  public static void completePhase(){
    completed = true;
    long end = System.currentTimeMillis();
    StatsComputer.end = end;
    logger.info(String.format("Wall time is %d ms", end - start));

  }

  public static boolean isCompleted() {
    return completed;
  }
}
