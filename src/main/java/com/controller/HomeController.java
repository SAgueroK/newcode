package com.controller;

import com.entity.DiscussPost;
import com.entity.Page;
import com.entity.User;
import com.service.DiscussPostSevice;
import com.service.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private DiscussPostSevice discussPostSevice;

    @RequestMapping(path = "/index",method = RequestMethod.GET)
    public String getIndexPage(Model model, Page page){
        page.setRows(discussPostSevice.findDiscussRows(0));
        page.setPath("/index");
        List<DiscussPost> discussPostslist = discussPostSevice.findDiscussPost(0,page.getOffset(),page.getLimit());
        List<Map<String, Object>> mapList = new ArrayList<>();
        if(discussPostslist!=null){
            for(DiscussPost post : discussPostslist){
                Map<String, Object> stringObjectMap = new HashMap<>();
                stringObjectMap.put("discusspost",post);
                User user = userSevice.findUserById(post.getUserId());
                stringObjectMap.put("user",user);
                mapList.add(stringObjectMap);
            }
        }
        model.addAttribute("discussPost",mapList);

        return "index";
    }
    @RequestMapping(path = "/error",method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }



}
