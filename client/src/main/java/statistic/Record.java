package statistic;

import com.mashape.unirest.http.HttpMethod;
import util.SystemClock;

/**
 * @author create by Xiao Han 10/5/19
 * @version 1.0
 * @since jdk 1.8
 */
public class Record {
  private int start;
  private HttpMethod method;
  private long latency;
  private int statusCode;
  private long timeStamp;

  public Record(int start, HttpMethod method, long latency, int statusCode) {
    this.start = start;
    this.method = method;
    this.latency = latency;
    this.statusCode = statusCode;
    this.timeStamp = SystemClock.now();
  }

  public int getStart() {
    return start;
  }

  public void setStart(int start) {
    this.start = start;
  }

  public HttpMethod getMethod() {
    return method;
  }

  public void setMethod(HttpMethod method) {
    this.method = method;
  }

  public long getLatency() {
    return latency;
  }

  public void setLatency(long latency) {
    this.latency = latency;
  }

  public int getStatusCode() {
    return statusCode;
  }

  public void setStatusCode(int statusCode) {
    this.statusCode = statusCode;
  }

  public long getTimeStamp() {
    return timeStamp;
  }

  @Override
  public String toString() {
    return start + "," + method + "," + latency + "," + statusCode;
  }
}
