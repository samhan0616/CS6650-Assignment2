package dao;

import entity.APIStatsEndpointStats;
import entity.StatisPojo;
import util.HikaricpUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatisticDaoImpl implements IStatisticDao {

  @Override
  public List<APIStatsEndpointStats> getStatistic() {
    Connection connection = null;
    try {
      connection = HikaricpUtils.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    List<APIStatsEndpointStats> res = new ArrayList<>();
    String sql = "select * from Upic_Statistic_General";
    Statement statement = null;
    ResultSet rs = null;
    try {
      statement = connection.createStatement();
      rs = statement.executeQuery(sql);

      while (rs.next()) {
        APIStatsEndpointStats apiStatsEndpointStats = new APIStatsEndpointStats(
                rs.getString("url"), rs.getString("operation"), rs.getDouble("mean"), rs.getInt("max"));
        res.add(apiStatsEndpointStats);
      }

      //JDBCUitl.JDBCClose(connection, preparedStatement, null);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      HikaricpUtils.releaseResources(connection, statement, rs);

    }
    return res;
  }

  @Override
  public void updateStatistic(List<StatisPojo> StatisPojos) {
    Connection connection = null;
    ResultSet rs = null;
    try {
      connection = HikaricpUtils.getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    HashMap<StatisPojo, long[]> statistic = new HashMap<>();
    for(StatisPojo record : StatisPojos) {
      if (!statistic.containsKey(record)) statistic.put(record, new long[3]);
      long[] arr = statistic.get(record);
      arr[0] += record.getLatency();
      arr[1] ++;
      arr[2] = Math.max(record.getLatency(), arr[2]);
      statistic.put(record, arr);
    }
    String querySql = "select * from Upic_Statistic_General where url=? and operation=?";
    String updateSql = "update Upic_Statistic_General set version=version+1, mean=?, max=?,total=? where url=? and operation=? and version=?";
    PreparedStatement preparedStatement = null;
    for (Map.Entry<StatisPojo, long[]> entry : statistic.entrySet()) {
      try {
       while (true) {
         preparedStatement = connection.prepareStatement(querySql);
         preparedStatement.setString(1, entry.getKey().getUrl());
         preparedStatement.setString(2, entry.getKey().getType());
         rs = preparedStatement.executeQuery();
         if (rs.next()) {
           int total = rs.getInt("total");
           int version = rs.getInt("version");
           double mean = rs.getDouble("mean");
           int max = rs.getInt("max");
           mean = (mean * total + entry.getValue()[0]) / (entry.getValue()[1] + total);
           max = (int)Math.max(max, entry.getValue()[2]);
           preparedStatement.close();
           rs.close();
           preparedStatement = connection.prepareStatement(updateSql);
           preparedStatement.setDouble(1, mean);
           preparedStatement.setInt(2,  max);
           preparedStatement.setInt(3, (int)(total + entry.getValue()[1]));
           preparedStatement.setString(4, entry.getKey().getUrl());
           preparedStatement.setString(5, entry.getKey().getType());
           preparedStatement.setInt(6, version);
           int row = preparedStatement.executeUpdate();
           preparedStatement.close();
           preparedStatement = null;
           if (row != 0) break;
         } else {
           String insert = "insert into Upic_Statistic_General values(?,?,?,?,?,?)";
           preparedStatement = connection.prepareStatement(insert);
           preparedStatement.setString(1, entry.getKey().getUrl());
           preparedStatement.setString(2, entry.getKey().getType());
           preparedStatement.setDouble(3, entry.getValue()[0] * 1.0/ entry.getValue()[1]);
           preparedStatement.setInt(4, (int)entry.getValue()[2]);
           preparedStatement.setInt(5, (int)entry.getValue()[1]);
           preparedStatement.setInt(6, 1);
           int row = preparedStatement.executeUpdate();
           preparedStatement.close();
           preparedStatement = null;
           if (row != 0) break;
         }

       }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    HikaricpUtils.releaseResources(connection, preparedStatement, rs);
  }




}
