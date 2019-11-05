package client.jobs;

import client.ExecutorContext;
import client.http.Result;
import com.mashape.unirest.http.HttpMethod;
import constant.PhaseEnum;
import constant.message.APIMessage;
import io.swagger.client.ApiException;
import io.swagger.client.ApiResponse;
import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import statistic.Record;
import statistic.RecordContainer;
import util.HttpUtil;
import util.SystemClock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author create by Xiao Han 10/1/19
 * @version 1.0
 * @since jdk 1.8
 */
public class SkierThread implements Runnable {

  private static Logger logger = LoggerFactory.getLogger(SkierThread.class);

  public static final int RESORT_ID = 1;
  public static final String SEASON_ID = "1";
  public static final String DAY_ID = "1";
  public static final int SUCCESS_STATUS_CODE = 201;

  private int skierIDStart;
  private int skierIDEnd;
  private int requestPerThread;
  private int timeStart;
  private int timeEnd;
  private SkiersApi skiersApi;
  private CountDownLatch particalCD;
  private CountDownLatch taskCD;
  private PhaseEnum phaseEnum;
  private ThreadLocalRandom random;

  public SkierThread(int index, int requestPerThread, int skierIDGap, int timeStart,
                     int timeEnd, CountDownLatch particalCD, PhaseEnum phaseEnum, CountDownLatch taskCD) {
    this.skierIDStart = index * skierIDGap + 1;
    this.skierIDEnd = (index + 1) * skierIDGap;
    this.requestPerThread = requestPerThread;
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.skiersApi = HttpUtil.skiersApi(ExecutorContext.address);
    this.particalCD = particalCD;
    this.taskCD = taskCD;
    this.phaseEnum = phaseEnum;
    this.random = ThreadLocalRandom.current();

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
  public void run() {
    for (int i = 0; i < requestPerThread; i++) {
      LiftRide liftRide = new LiftRide();
      int time = random.nextInt(timeEnd - timeStart) + timeStart;
      int lift = random.nextInt(ExecutorContext.numLifts) + 1;
      int skierId = random.nextInt(skierIDEnd - skierIDStart) + skierIDStart;
      liftRide.setLiftID(lift);
      liftRide.setTime(time);
      try {
        long start = SystemClock.now();
        ApiResponse<Void> response = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, RESORT_ID, SEASON_ID, DAY_ID, skierId);
        int statusCode = response.getStatusCode();
        long complete = SystemClock.now();
        //logger.info("post takes " + (complete - start));
        RecordContainer.enqueue(new Record(time, HttpMethod.POST, complete - start, statusCode));

        if (statusCode != SUCCESS_STATUS_CODE) {
          logger.info(String.format(APIMessage.API_FAILED_LOG, statusCode));
        } else if (phaseEnum == PhaseEnum.phase3) {
          start = SystemClock.now();
          doQuery(RESORT_ID, SEASON_ID, DAY_ID, skierId);
          complete = SystemClock.now();
          RecordContainer.enqueue(new Record(time, HttpMethod.POST, complete - start, statusCode));
        }

      } catch (ApiException e) {
        e.printStackTrace();
        logger.error(APIMessage.ERROR_LOG);
        i--;
      }
    }
    if (particalCD != null) particalCD.countDown();
    taskCD.countDown();
  }

  /**
   * do query in phase 3
   */
  private void doQuery(Integer resortID, String seasonID, String dayID, Integer skierID) {
    try {

      Integer response = skiersApi.getSkierDayVertical(resortID, seasonID, dayID, skierID);
      //logger.info(String.format("The total vertical for the skier id: %s is %d", skierID, response.getData()));
      //logger.info("query takes " + (System.currentTimeMillis() - now));
    } catch (ApiException e) {
      e.printStackTrace();
      logger.info("Failed to get the total vertical for the skier for the specified ski day");
    }
  }
}


