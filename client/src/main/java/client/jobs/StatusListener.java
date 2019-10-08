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


  public static final int MAX_PHRASE = 3;
  private static int completed;
  public static long start;

  static {
    completed = 0;
  }

  public static void completePhrase(){
    completed++;
    if (completed == MAX_PHRASE) {
      long end = System.currentTimeMillis();
      StatsComputer.end = end;
      logger.info(String.format("Wall time is %d ms", end - start));
    }

  }

  public static boolean isCompleted() {
    return completed == MAX_PHRASE;
  }
}
