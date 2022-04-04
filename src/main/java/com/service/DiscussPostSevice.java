package com.service;

import com.dao.DiscussPostMapper;
import com.domain.DiscussPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class DiscussPostSevice {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    public List<DiscussPost> findDiscussPost(int uid,int offset,int lim){
        return discussPostMapper.selectDiscussPosts(uid,offset,lim);
    }
    public int findDiscussRows(int uid){
        return discussPostMapper.selectDiscussPostRows(uid);
    }
}
