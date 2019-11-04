package dao;

import entity.Skier;

import java.util.List;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public interface ISkierDao {


  void createSkierHistory(Skier skier);

  void createSkierHistoryBatch(List<Skier> skiers);

  Integer getTotalVertical(Integer resortID, String seasonID, String dayID, Integer skierID);

  void createSkierMV(long curr, long gap);
}
