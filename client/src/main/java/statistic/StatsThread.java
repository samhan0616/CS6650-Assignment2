package statistic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;

/**
 * @author create by Xiao Han 10/6/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatsThread implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(StatsThread.class);

  private Writer csvWriter;
  private StatsComputer computer;

  public StatsThread(Writer csvWriter, StatsComputer computer) {
    this.csvWriter = csvWriter;
    this.computer = computer;
  }

  /**
   * When an object implementing interface <code>Runnable</code> is used
   * to create a thread, starting the thread causes the object's
   * <code>run</code> method to be called in that separately executing
   * thread.
   * <p>
   * The general contract of the method <code>run</code> is that it may
   * take any action whatsoever.
   *
   * @see Thread#run()
   */
  @Override
  public void run() {
    while (!StatusListener.isCompleted()) {
      Record record = RecordContainer.dequeue();
      if (record == null) continue;
      try {
        csvWriter.write(record.toString() + System.getProperty("line.separator"));
        computer.update(record);
      } catch (IOException e) {
        //e.printStackTrace();
        logger.error("CSV writer I/O-write exception");
      }
    }
    logger.info("record CSV completed");
    try {
      csvWriter.close();
    } catch (IOException e) {
      logger.error("CSV writer I/O-close exception");
    }
    computer.complete();
  }
}
