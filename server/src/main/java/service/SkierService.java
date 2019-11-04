package service;

import com.google.gson.JsonSyntaxException;
import dao.ISkierDao;
import dao.SkierDaoImpl;
import entity.LiftRide;
import entity.Skier;
import entity.StatisPojo;
import service.middle.IQueueService;
import service.middle.LocalQueueService;
import service.middle.SkierViewService;
import service.middle.StatisticQueueService;
import util.GsonUtil;
import util.StringUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public class SkierService implements ISkierService{

  public static final int PARAM_INVALID_CODE = 400;
  public static final int SUCCESS_CODE = 201;

  private IQueueService<Skier> queueService;
  private ISkierDao iSkierDao;
  private IQueueService<StatisPojo> statisPojoIQueueService;

  public SkierService() {
    queueService = new LocalQueueService();
    iSkierDao = new SkierDaoImpl();
    statisPojoIQueueService = StatisticQueueService.instance;
    //start refresh task
    new SkierViewService();
  }

  public void createSkierHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String uri = req.getRequestURI();
    BufferedReader br = req.getReader();
    String str = "";
    StringBuilder json = new StringBuilder();
    while((str = br.readLine()) != null){
      json.append(str);
    }
    br.close();
    LiftRide liftRide = null;
    try{
      liftRide = GsonUtil.fromJson(json.toString(), LiftRide.class);
    } catch (JsonSyntaxException e) {
      writeErrorMsg(resp);
      return;
    }

    String[] paras = uri.split("/");
    if (!isValid(paras)) {
      writeErrorMsg(resp);
      return;
    }

    Skier skier = new Skier(Integer.parseInt(paras[2]), paras[4], paras[6],
            Integer.parseInt(paras[8]), liftRide.getTime(), liftRide.getLiftID() * 10);
    queueService.enqueue(skier);
    //iSkierDao.createSkierHistory(skier);
    resp.setStatus(SUCCESS_CODE);
//    resp.setContentType("application/json;charset=utf-8");
//    resp.getWriter().write(GsonUtil.toJson(Result.success()));
  }

  @Override
  public void getTotalVertical(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    long curr = System.currentTimeMillis();
    String uri = req.getRequestURI();
    String[] paras = uri.split("/");
    if (!isValid(paras)) {
      writeErrorMsg(resp);
      return;
    }
    PrintWriter printWriter = resp.getWriter();
    long start = System.currentTimeMillis();
    Integer res = iSkierDao.getTotalVertical(Integer.parseInt(paras[2]),
            paras[4], paras[6], Integer.parseInt(paras[8]));
    long end = System.currentTimeMillis();
    statisPojoIQueueService.enqueue(new StatisPojo("GET", "/skiers", end - start));
    //printWriter.write(GsonUtil.toJson(Result.success(res, null)));
    printWriter.write(String.valueOf(res));
    printWriter.close();
  }


  private boolean isValid(String[] paras) {
    return StringUtil.isNum(paras[2]) && StringUtil.isNum(paras[8]);
  }


  private void writeErrorMsg(HttpServletResponse resp) throws IOException {
    resp.setStatus(PARAM_INVALID_CODE);
//    resp.setContentType("application/json;charset=utf-8");
//    PrintWriter printWriter = resp.getWriter();
//    printWriter.write(GsonUtil.toJson(Result.error(PARAM_INVALID_CODE, null)));
  }
}
