package com.controller;

import com.annotation.LoginRequired;
import com.entity.DiscussPost;
import com.entity.Page;
import com.entity.User;
import com.service.DiscussPostSevice;
import com.service.LikeService;
import com.service.UserSevice;
import com.util.CommunityConstant;
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
public class HomeController implements CommunityConstant {
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private DiscussPostSevice discussPostSevice;
    @Autowired
    private LikeService likeService;

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
                long likeCount =likeService.findEntityLikeCount(ENTITY_TYPE_POST,post.getId());
                stringObjectMap.put("likeCount",likeCount);
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
