package cn.zjiali.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author zJiaLi
 * @since 2021-12-04 11:17
 */
@Controller
public class IndexController  {


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping(value = {"/", "index.html"})
    public String index() {
        return "index";
    }


}
