package com.controller;

import com.entity.User;
import com.google.code.kaptcha.Producer;
import com.service.UserSevice;
import com.util.CommunityConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpCookie;
import java.net.http.HttpRequest;
import java.util.Map;

@Controller
public class LoginController implements CommunityConstant {
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private Producer kaptchaproducer;
    @RequestMapping(path = "/register",method = RequestMethod.GET)
    public String getRegisterPage(){
        return "/site/register";
    }
    @RequestMapping(path = "/login",method = RequestMethod.GET)
    public String getLoginPage(){
        return "/site/login";
    }
    @RequestMapping(path = "/kaptcha",method = RequestMethod.GET)
    public void getkaptcha(HttpServletResponse response, HttpSession session) {
        String text =kaptchaproducer.createText();
        BufferedImage image = kaptchaproducer.createImage(text);
        //验证码存入session
        session.setAttribute("kaptcha",text);
        //图片输出给浏览器
        response.setContentType("image/png");
        try {
            OutputStream out = response.getOutputStream();
            ImageIO.write(image,"png",out);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(path = "/register",method = RequestMethod.POST)
    private String register(Model model, User user){
        Map<String,Object> map =userSevice.register(user);
        if(map==null||map.isEmpty()){
            model.addAttribute("msg","注册成功,请激活");
            model.addAttribute("target","/index");
            return "/site/operate-result";
        }
        else{
            model.addAttribute("usernameMsg",map.get("usernameMsg"));
            model.addAttribute("passwordMsg",map.get("passwordMsg"));
            model.addAttribute("emailMsg",map.get("emailMsg"));
            return "/site/register";
        }

    }
    @RequestMapping(path = "/activation/{userId}/{code}",method = RequestMethod.GET)
    public String activation(Model model, @PathVariable("userId") int userId,@PathVariable("code") String  code){
        int result = userSevice.activation(userId,code);
        if(result==ACTIVATION_SUCCESS){
            model.addAttribute("msg","激活成功");
            model.addAttribute("target","/login");
        }else if(result==ACTIVATION_REPEAT){
            model.addAttribute("msg","重复激活");
            model.addAttribute("target","/index");
        }else {
            model.addAttribute("msg","激活失败");
            model.addAttribute("target","/index");
        }
        return "/site/operate-result";
    }
}
