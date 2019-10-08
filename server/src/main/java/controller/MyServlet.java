package controller;

import com.google.gson.Gson;
import controller.entity.Skier;
import controller.util.GsonUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;

public class MyServlet extends HttpServlet {

    int num;
    /**
     * need to do
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // do something with uri
        //String uri = req.getRequestURI();
        //System.out.println(uri);
        BufferedReader br = req.getReader();
        String str = "";
        StringBuilder json = new StringBuilder();
        while((str = br.readLine()) != null){
            json.append(str);
        }
        br.close();
        Skier skier = GsonUtil.fromJson(json.toString(), Skier.class);
        inc();
        resp.setStatus(201);
        resp.getWriter().write("OK");
    }

    synchronized void inc() {
        num ++;
        System.out.println(num);
    }

}
