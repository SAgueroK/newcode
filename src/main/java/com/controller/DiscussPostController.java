package com.controller;

import com.dao.DiscussPostMapper;
import com.entity.DiscussPost;
import com.entity.User;
import com.service.DiscussPostSevice;
import com.service.UserSevice;
import com.util.HostHolder;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.Date;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostSevice sevice;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserSevice userSevice;
    @RequestMapping(path = "/add" ,method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title,String content){
        //先尝试获取threadlocal中的user，看有没有登录
        User user = hostHolder.getUsers();
        if(user==null){
            return RandomUtil.getJsonString(403,"还没有登录");
        }
        DiscussPost discussPost = new DiscussPost();
        discussPost.setUserId(user.getId());
        discussPost.setTitle(title);
        discussPost.setContent(content);
        discussPost.setCreateTime(new Date());
        sevice.addDiscussPost(discussPost);
        //报错未处理
        return RandomUtil.getJsonString(0,"发布成功");
    }
    @RequestMapping(path = "/detail/{discussPostId}" , method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model){
        //帖子
        DiscussPost discussPost=sevice.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
        User user = userSevice.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);

        return "/site/discuss-detail";
    }
}
