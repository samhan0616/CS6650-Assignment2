package dao;

import entity.Skier;
import entity.StatisPojo;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import service.middle.StatisticQueueService;
import util.DBConnectionPoolUtil;
import util.JDBCUitl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public class SkierDaoImpl implements ISkierDao {

  @Override
  public void createSkierHistory(Skier skier) {
    Connection connection = null;
    try {
      connection = DBConnectionPoolUtil.getInstance().getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      connection = JDBCUitl.getConnection();
    }

    String sql = "insert into Upic_Skier_History values(?,?,?,?,?,?)";
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, skier.getResortID());
      preparedStatement.setString(2, skier.getSeasonID());
      preparedStatement.setString(3, skier.getDayID());
      preparedStatement.setInt(4, skier.getSkierID());
      preparedStatement.setInt(5, skier.getTime());
      preparedStatement.setInt(6, skier.getLiftID());
      preparedStatement.executeUpdate();
      //JDBCUitl.JDBCClose(connection, preparedStatement, null);
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBConnectionPoolUtil.releaseAll(connection, preparedStatement, null);
    }
  }

  @Override
  public void createSkierHistoryBatch(List<Skier> skiers) {
    long start = System.currentTimeMillis();
    if (skiers.size() == 0) return;
    Connection connection = null;
    try {
      connection = DBConnectionPoolUtil.getInstance().getConnection();
      //connection.setAutoCommit(false);
    } catch (SQLException e) {
      e.printStackTrace();
      connection = JDBCUitl.getConnection();
    }

    //String sql = "insert into Upic_Skier_History values(?,?,?,?,?,?)";
    StringBuilder sql = new StringBuilder("insert into Upic_Skier_History values ");
    Statement statement = null;
    try {
      //PreparedStatement preparedStatement = connection.prepareStatement(sql);
      statement = connection.createStatement();

//      for (Skier skier : skiers) {
//        preparedStatement.setInt(1, skier.getResortID());
//        preparedStatement.setString(2, skier.getSeasonID());
//        preparedStatement.setString(3, skier.getDayID());
//        preparedStatement.setInt(4, skier.getSkierID());
//        preparedStatement.setInt(5, skier.getTime());
//        preparedStatement.setInt(6, skier.getLiftID());
//        preparedStatement.addBatch();
//      }
      for (int i = 0 ; i < skiers.size(); i ++) {
        sql.append(skiers.get(i).toString());
        if (i != skiers.size() - 1) {
          sql.append(",");
        }
      }
//      preparedStatement.executeBatch();
      int res = statement.executeUpdate(sql.toString());
      if (res == 0) return;
      StatisticQueueService.instance.enqueue(new StatisPojo("POST", "/skiers", System.currentTimeMillis() - start));
      //JDBCUitl.JDBCClose(connection, preparedStatement, null);
    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
      DBConnectionPoolUtil.releaseAll(connection, statement, null);
    }

  }

  @Override
  public Integer getTotalVertical(Integer resortID, String seasonID, String dayID, Integer skierID) {
    Connection connection = null;
    try {
      connection = DBConnectionPoolUtil.getInstance().getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      connection = JDBCUitl.getConnection();
    }
    ResultSet resultSet = null;
    //String sql = "Select sum(liftID) as total from Upic_Skier_History where resortID = ? and seasonID = ? and dayID = ? and skierID = ?";
    String sql = "Select totalLift from Upic_Skier_History_MV where resortID = ? and seasonID = ? and dayID = ? and skierID = ?";
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setInt(1, resortID);
      preparedStatement.setString(2, seasonID);
      preparedStatement.setString(3, dayID);
      preparedStatement.setInt(4, skierID);
      resultSet = preparedStatement.executeQuery();
      Integer res = null;
      while(resultSet.next()) {
        res = resultSet.getInt("totalLift");
      }
      //JDBCUitl.JDBCClose(connection, preparedStatement, resultSet);
      return res;
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBConnectionPoolUtil.releaseAll(connection, preparedStatement, resultSet);
    }

    return null;
  }

  @Override
  public void createSkierMV(long curr, long gap) {
    Connection connection = null;
    try {
      connection = DBConnectionPoolUtil.getInstance().getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
      connection = JDBCUitl.getConnection();
    }
      String sql = "update Upic_Skier_History_Update_Lock set version = version + 1, last_update = " + curr +
            " where last_update <= " + (curr - gap);
    Statement statement = null;
    try {
      statement = connection.createStatement();
      System.out.println("sql " + sql);
      int res = statement.executeUpdate(sql);

      if (res == 0) return;
      //statement.execute("truncate Upic_Skier_History_MV");
      statement.execute("INSERT INTO Upic_Skier_History_MV\n" +
              "select a.* from (SELECT resortID, seasonID, dayID, skierID, sum(lift) as total " +
              "FROM Upic_Skier_History GROUP BY resortID, seasonID, dayID, skierID) as a\n" +
              "ON DUPLICATE KEY UPDATE totalLift=total");
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      DBConnectionPoolUtil.releaseAll(connection, statement, null);
    }
  }
}
