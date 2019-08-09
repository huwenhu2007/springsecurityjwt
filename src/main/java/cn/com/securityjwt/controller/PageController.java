package cn.com.securityjwt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 网页控制器
 * @Author huwenhu
 * @Date 2019/8/9 10:31
 **/
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping(value = "/tag")
    public String securityTag(){
        return "securityTag";
    }


}
