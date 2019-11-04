package service;

import entity.APIStatsEndpointStats;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public interface IStatisticService {

  void getStats(HttpServletResponse response) throws IOException;
}
