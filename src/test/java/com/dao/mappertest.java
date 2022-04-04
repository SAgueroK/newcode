package com.dao;

import com.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class mappertest {
    @Autowired
    private DiscussPostMapper discussPostMapper;
    @Autowired
    private UserMapper userMapper;
    @Test
    public void testdiscuss(){
        /*val user = new User();
        user.setEmail("sdad");
        user.setActivationCode("sdadsada");
        user.setHeaderUrl("dsada");
        user.setPassword("ddd");
        user.setSalt("d");
        user.setStatus(2);
        user.setType(2);
        user.setUsername("ddd");
        userMapper.insertUser(user);*/

    }
}
