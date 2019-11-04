package service.middle;

import dao.IStatisticDao;
import dao.StatisticDaoImpl;
import entity.StatisPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatisticQueueService implements IQueueService<StatisPojo> {

  private static Logger logger = LoggerFactory.getLogger(StatisticQueueService.class);

  private IStatisticDao statisticDao;

  private LinkedBlockingQueue<StatisPojo> linkedBlockingQueue;

  public static StatisticQueueService instance = SingletonHandler.singleton;

  private static class SingletonHandler {
    private static StatisticQueueService singleton = new StatisticQueueService();
  }


  public StatisticQueueService() {
    this.linkedBlockingQueue = new LinkedBlockingQueue<>();
    this.statisticDao = new StatisticDaoImpl();
    startTimer();
  }

  private void startTimer() {
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
          }
          int size = linkedBlockingQueue.size();
          if (size == 0) continue;
          List<StatisPojo> statisPojos = dequeue(size);
          statisticDao.updateStatistic(statisPojos);
        }
      }
    }).start();
  }
  @Override
  public void enqueue(StatisPojo statisPojo) {
    linkedBlockingQueue.offer(statisPojo);
  }

  @Override
  public List<StatisPojo> dequeue(int num) {
    List<StatisPojo> statisPojos = new ArrayList<>();
    for (int i = 0; i < num && !linkedBlockingQueue.isEmpty(); i++) {
      statisPojos.add(linkedBlockingQueue.remove());
    }
    return statisPojos;
  }
}
