package client.jobs;

import client.ExecutorContext;
import com.mashape.unirest.http.HttpMethod;
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
  private CountDownLatch countDownLatch;

  public SkierThread(int index, int requestPerThread, int skierIDGap, int timeStart, int timeEnd, CountDownLatch countDownLatch) {
    this.skierIDStart = index * skierIDGap + 1;
    this.skierIDEnd = (index + 1) * skierIDGap;
    this.requestPerThread = requestPerThread;
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.skiersApi = HttpUtil.skiersApi(ExecutorContext.address);
    this.countDownLatch = countDownLatch;
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
      ThreadLocalRandom random = ThreadLocalRandom.current();
      int time = random.nextInt(timeEnd - timeStart) + timeStart;
      int lift = random.nextInt(ExecutorContext.numLifts) + 1;
      int skierId = random.nextInt(skierIDEnd - skierIDStart) + skierIDStart;
      liftRide.setLiftID(lift);
      liftRide.setTime(time);
      try {
        long start = System.currentTimeMillis();
        ApiResponse<Void> response = skiersApi.writeNewLiftRideWithHttpInfo(liftRide, RESORT_ID, SEASON_ID, DAY_ID, skierId);
        int statusCode = response.getStatusCode();
        long complete = System.currentTimeMillis();
        RecordContainer.enqueue(new Record(time, HttpMethod.POST, complete - start, statusCode));

        if (statusCode != SUCCESS_STATUS_CODE) {
          logger.info(String.format(APIMessage.API_FAILED_LOG, statusCode));
        }
      } catch (ApiException e) {
        e.printStackTrace();
        logger.error(APIMessage.ERROR_LOG);
        i--;
      }
    }
    countDownLatch.countDown();
  }
}


