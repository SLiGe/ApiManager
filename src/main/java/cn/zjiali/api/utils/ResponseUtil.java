package cn.zjiali.api.utils;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zJiaLi
 * @since 2021-12-04 18:26
 */
public class ResponseUtil {

    public static void out(HttpServletResponse response, Object data) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        out.write(JsonUtil.toJson(data));
        out.flush();
        out.close();
    }
}
