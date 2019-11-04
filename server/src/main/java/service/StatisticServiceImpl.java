package service;

import dao.IStatisticDao;
import dao.StatisticDaoImpl;
import entity.APIStatsEndpointStats;
import entity.StatisPojo;
import service.middle.IQueueService;
import service.middle.StatisticQueueService;
import util.GsonUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatisticServiceImpl implements IStatisticService {

  private IStatisticDao statisticDao;
  private IQueueService<StatisPojo> statisPojoIQueueService;

  public StatisticServiceImpl() {
    this.statisticDao = new StatisticDaoImpl();
    statisPojoIQueueService = StatisticQueueService.instance;
  }

  @Override
  public void getStats(HttpServletResponse response) throws IOException {
    long start = System.currentTimeMillis();
    List<APIStatsEndpointStats> res =  statisticDao.getStatistic();
    long end = System.currentTimeMillis();
    statisPojoIQueueService.enqueue(new StatisPojo("GET", "/statistics" , end - start));
    response.setContentType("application/json");
    String json = GsonUtil.toJson(res);
    PrintWriter printWriter = response.getWriter();
    printWriter.write(json);
    printWriter.close();

  }
}
