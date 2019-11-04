package entity;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class APIStatsEndpointStats {

  private String URL = null;

  private String operation = null;

  private Double mean = null;

  private Integer max = null;

  public APIStatsEndpointStats(String URL, String operation, Double mean, Integer max) {
    this.URL = URL;
    this.operation = operation;
    this.mean = mean;
    this.max = max;
  }
}
