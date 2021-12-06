package cn.zjiali.api.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 视图全局变量配置
 *
 * @author zJiaLi
 * @since 2021-12-06 20:33
 */
@ControllerAdvice(annotations = {Controller.class})
public class ViewGlobalVarControllerAdvice {

    @ModelAttribute(name = "site")
    public ModelMap modelMap() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("siteName", "ApiManager");
        modelMap.addAttribute("version", "1.0.1");
        return modelMap;
    }
}
