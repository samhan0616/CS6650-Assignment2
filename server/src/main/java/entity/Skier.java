package entity;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class Skier {
  private Integer resortID;
  private String seasonID;
  private String dayID;
  private Integer skierID;
  private int time;
  private int liftID;

  public Skier(Integer resortID, String seasonID, String dayID, Integer skierID, int time, int liftID) {
    this.resortID = resortID;
    this.seasonID = seasonID;
    this.dayID = dayID;
    this.skierID = skierID;
    this.time = time;
    this.liftID = liftID;
  }

  public Integer getResortID() {
    return resortID;
  }

  public void setResortID(Integer resortID) {
    this.resortID = resortID;
  }

  public String getSeasonID() {
    return seasonID;
  }

  public void setSeasonID(String seasonID) {
    this.seasonID = seasonID;
  }

  public String getDayID() {
    return dayID;
  }

  public void setDayID(String dayID) {
    this.dayID = dayID;
  }

  public Integer getSkierID() {
    return skierID;
  }

  public void setSkierID(Integer skierID) {
    this.skierID = skierID;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getLiftID() {
    return liftID;
  }

  public void setLiftID(int liftID) {
    this.liftID = liftID;
  }

  @Override
  public String toString() {
    return "("+resortID + "," +seasonID + "," + dayID+ "," +skierID+ "," +time+ "," +(liftID*10) +")";
  }
}
