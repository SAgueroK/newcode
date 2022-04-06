package com.controller.interceptor;

import com.entity.LoginTicket;
import com.entity.User;
import com.service.UserSevice;
import com.util.CookieUil;
import com.util.HostHolder;
import org.apache.ibatis.plugin.Interceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Component
public class LoginTicketInterceptor implements HandlerInterceptor {
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private HostHolder hostHolder;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //从coolkie中获得凭证
        String ticket = CookieUil.getVaule(request,"ticket");
        if(ticket!=null){
            //查询凭证
            LoginTicket loginTicket = userSevice.findLoginTicket(ticket);
            //检查凭证
            if(loginTicket!=null&&loginTicket.getStatus()==0&&loginTicket.getExpired().after(new Date())){
                //获取user
                User user= userSevice.findUserById(loginTicket.getUserId());
                //user数据存入请求中
                hostHolder.setUsers(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        User user = hostHolder.getUsers();
        if(user!=null&&modelAndView!=null){
            modelAndView.addObject("loginUser",user);
            System.out.println(user);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        hostHolder.clear();
    }
}
