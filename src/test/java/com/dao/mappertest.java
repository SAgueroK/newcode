package com.dao;

import com.entity.DiscussPost;
import com.entity.LoginTicket;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;

@SpringBootTest
public class mappertest {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private LoginTicketMapper loginTicketMapper;
    @Test
    public void testdiscuss(){
        DiscussPost post =new DiscussPost();
        post.setCreateTime(new Date());
        post.setContent("dsad");
        post.setTitle("dsa");
        post.setUserId(12);
        post.setScore(3);
        post.setCommentCount(2);
        post.setStatus(1);

        discussPostMapper.insertDiscussPost(post);
    }
}
