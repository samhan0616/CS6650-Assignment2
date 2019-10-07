package controller.entity;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class Skier {
  private int time;
  private int liftID;

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
    return "Skier{" +
            "time=" + time +
            ", liftID=" + liftID +
            '}';
  }
}
