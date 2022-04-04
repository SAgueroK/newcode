package com.service;

import com.dao.DiscussPostMapper;
import com.dao.UserMapper;
import com.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserSevice {
    @Autowired
    private UserMapper userMapper;
    public User findUserById(int uid){
        return userMapper.selectByid(uid);
    }
}
