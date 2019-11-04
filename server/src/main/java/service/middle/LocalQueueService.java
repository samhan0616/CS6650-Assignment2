package service.middle;

import dao.ISkierDao;
import dao.SkierDaoImpl;
import entity.Skier;
import entity.StatisPojo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public class LocalQueueService implements IQueueService<Skier> {

  public static final int BATCH_SIZE = 5000;
  private static Logger logger = LoggerFactory.getLogger(LocalQueueService.class);

  private ISkierDao skierDao;

  private IQueueService<StatisPojo> statisPojoIQueueService;

  private LinkedBlockingQueue<Skier> linkedBlockingQueue;


  public LocalQueueService() {
    this.linkedBlockingQueue = new LinkedBlockingQueue<>();
    statisPojoIQueueService = StatisticQueueService.instance;
    this.skierDao = new SkierDaoImpl();
    startTimer();
  }

  private void startTimer() {
//    TimerTask task = new TimerTask() {
//      @Override
//      public void run() {
//        List<Skier> skiers = dequeue(BATCH_SIZE);
//        skierDao.createSkierHistoryBatch(skiers);
//      }
//    };
//    Timer timer = new Timer();
//    timer.scheduleAtFixedRate(task, 0, 10);
    new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          List<Skier> skiers = dequeue(BATCH_SIZE);
          long start = System.currentTimeMillis();
          skierDao.createSkierHistoryBatch(skiers);
          long diff = System.currentTimeMillis() - start;

          if (diff < 50) {
            try {
              Thread.sleep(2000);
            } catch (InterruptedException e) {
              logger.info("Cannot fall a sleep Zzzzz");
            }
          }
        }
      }
    }).start();
  }


  @Override
  public void enqueue(Skier skier) {
    this.linkedBlockingQueue.offer(skier);
  }

  @Override
  public List<Skier> dequeue(int num) {
    List<Skier> skiers = new ArrayList<>();
    for (int i = 0; i < num && !linkedBlockingQueue.isEmpty(); i++) {
      skiers.add(linkedBlockingQueue.remove());
    }
    return skiers;
  }



}
