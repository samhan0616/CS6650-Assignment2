package statistic;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatusListener {

  public static final int MAX_PHRASE = 3;
  private static int completed;

  static {
    completed = 0;
  }

  public static void completePhrase(){
    completed++;
    if (completed == MAX_PHRASE) {
      StatsComputer.end = System.currentTimeMillis();
    }
  }

  public static boolean isCompleted() {
    return completed == MAX_PHRASE;
  }
}
