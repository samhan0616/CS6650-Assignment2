package entity;

import java.util.Objects;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatisPojo {
  private String type;
  private String url;
  private long latency;

  public StatisPojo(String type, String url, long latency) {
    this.type = type;
    this.url = url;
    this.latency = latency;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    StatisPojo that = (StatisPojo) o;
    return Objects.equals(type, that.type) &&
            Objects.equals(url, that.url);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, url);
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }


}
