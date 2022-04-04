package com.dao;

import com.domain.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    User selectByid(int uid);

    User selectByName(String name);

    User selectByEmail(String email);

    int insertUser(User user);

    int updataStatus(int uid,int status);

    int updataHeardeUrl(int uid,String headerUrl);

    int updataPassWord(int uid,String passWord);

}
