package com.controller;

import com.annotation.LoginRequired;
import com.entity.User;
import com.service.LikeService;
import com.util.HostHolder;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
public class LikeController {
    @Autowired
    private LikeService likeService;

    @Autowired
    private HostHolder hostHolder;
    @LoginRequired
    @RequestMapping(path = "/like",method = RequestMethod.POST)
    @ResponseBody
    public String like(int entityType,int entityId,int entityUserId){
        User user=hostHolder.getUser();
        likeService.like(user.getId(), entityType,entityId, entityUserId);
        long likeCount=likeService.findEntityLikeCount(entityType,entityId);
        int liekStatus=likeService.findEntityLikeStatus(user.getId(), entityType,entityId);
        //θΏεη»ζ

        Map<String,Object> map =new HashMap<>();

        map.put("likeCount",likeCount);
        map.put("likeStatus",liekStatus);

        return RandomUtil.getJsonString(0,null,map);
    }

}
