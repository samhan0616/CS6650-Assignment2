package client.jobs;

/**
 * @author create by Xiao Han 10/1/19
 * @version 1.0
 * @since jdk 1.8
 */
public class PhaseLock {

  public static final Object phase2Lock = new Object();
  public static final Object phase3Lock = new Object();

}
