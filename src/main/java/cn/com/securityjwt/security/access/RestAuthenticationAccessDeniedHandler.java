package cn.com.securityjwt.security.access;

import cn.com.securityjwt.entity.ResultJson;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 权限不足处理类
 * @Author huwenhu
 * @Date 2019/8/7 11:42
 **/
@Component(value="restAuthenticationAccessDeniedHandler")
public class RestAuthenticationAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        //登陆状态下，权限不足执行该方法
        System.out.println("权限不足：" + e.getMessage());
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = httpServletResponse.getWriter();
        String body = ResultJson.build(-1, e.getMessage()).toJSONString();
        printWriter.write(body);
        printWriter.flush();
    }
}
