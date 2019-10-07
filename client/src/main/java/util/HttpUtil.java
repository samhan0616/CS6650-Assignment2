package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequestWithBody;
import io.swagger.client.ApiClient;
import io.swagger.client.api.ResortsApi;
import io.swagger.client.api.SkiersApi;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpUtil {

  private static Logger logger = LoggerFactory.getLogger(HttpUtil.class);


  public HttpUtil() {
  }


  /**
   *
   * @param baseUrl
   * @return
   */
  public static ApiClient apiClient(String baseUrl) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    return apiClient;
  }

  /**
   *
   * @param baseUrl
   * @return
   */
  public static ResortsApi resortsApi(String baseUrl) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    return new ResortsApi(apiClient);
  }

  /**
   *
   * @param baseUrl
   * @return
   */
  public static SkiersApi skiersApi(String baseUrl) {
    ApiClient apiClient = new ApiClient();
    apiClient.setBasePath(baseUrl);
    return new SkiersApi(apiClient);
  }
}