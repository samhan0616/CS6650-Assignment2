package service;

import entity.Skier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author create by Xiao Han 10/22/19
 * @version 1.0
 * @since jdk 1.8
 */
public interface ISkierService {

  void createSkierHistory(HttpServletRequest req, HttpServletResponse resp) throws IOException;

  void getTotalVertical(HttpServletRequest req, HttpServletResponse resp) throws IOException;
}
