package cn.com.securityjwt.controller;

import cn.com.securityjwt.entity.ResponseUserToken;
import cn.com.securityjwt.entity.UserLoginDTO;
import cn.com.securityjwt.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author huwenhu
 * @Date 2019/8/7 12:06
 **/
@RestController
@RequestMapping("/auth")
public class UserController {

    @Value("${jwt.header}")
    private String header;;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login")
    public ResponseUserToken login(UserLoginDTO user, HttpServletResponse httpServletResponse){
        ResponseUserToken response = userService.login(user.getUsername(), user.getPassword());

        Cookie cookie = new Cookie(header, response.getToken());
        cookie.setMaxAge(24 * 60 * 60);
        cookie.setPath("/");
        httpServletResponse.addCookie(cookie);

        return response;
    }

    @RequestMapping(value = "/token_test")
    public String tokenTest(){
        return "OK";
    }

}
