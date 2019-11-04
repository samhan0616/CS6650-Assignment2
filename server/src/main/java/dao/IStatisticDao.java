package dao;

import entity.APIStatsEndpointStats;
import entity.StatisPojo;

import java.util.List;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public interface IStatisticDao {

  List<APIStatsEndpointStats> getStatistic();

  void updateStatistic(List<StatisPojo> StatisPojos);
}
