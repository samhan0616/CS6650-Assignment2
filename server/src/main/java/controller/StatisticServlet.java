package controller;

import service.IStatisticService;
import service.StatisticServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author create by Xiao Han 11/3/19
 * @version 1.0
 * @since jdk 1.8
 */
public class StatisticServlet extends HttpServlet {

  private IStatisticService statisticService;

  public StatisticServlet() {
    this.statisticService = new StatisticServiceImpl();
  }
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    statisticService.getStats(resp);
  }
}
