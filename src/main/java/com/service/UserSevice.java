package com.service;

import com.dao.UserMapper;
import com.entity.User;
import com.util.CommunityConstant;
import com.util.MailClient;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class UserSevice implements CommunityConstant{
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MailClient mailClient;
    @Autowired
    private TemplateEngine templateEngine;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextpath;

    public User findUserById(int uid){
        return userMapper.selectByid(uid);
    }
    public Map<String,Object> register(User user){
        Map<String,Object> map =new HashMap<>();
        //判空
        if(user==null){
            throw new IllegalArgumentException("参数不能为空");
        }
        if(StringUtils.isEmpty(user.getUsername())){
            map.put("usernameMsg","账号不能为空");
            return map;
        }
        if(StringUtils.isEmpty(user.getPassword())){
            map.put("passwordMsg","密码不能为空");
            return map;
        }
        if(StringUtils.isEmpty(user.getEmail())){
            map.put("emailMsg","邮箱不能为空");
            return map;
        }
        //验证账号
        User u =userMapper.selectByName(user.getUsername());
        if(u!=null){
            map.put("usernameMsg","账号已存在");
            return map;
        }
        //验证邮箱
        u=userMapper.selectByEmail(user.getEmail());
        if(u!=null){
            map.put("emailMsg","邮箱已存在");
            return map;
        }
        //注册用户,随机获取uuid获取前五位
        user.setSalt(RandomUtil.generateUUID().substring(0,5));
        user.setPassword(RandomUtil.md5(user.getPassword()+user.getSalt()));
        user.setType(0);
        user.setStatus(0);
        user.setActivationCode(RandomUtil.generateUUID());
        user.setHeaderUrl(String.format("https://images.nowcoder.com/head/%dt.png",
                new Random().nextInt(1000)));
        user.setCreatTime(new Date());
        userMapper.insertUser(user);
        //激活邮件
        Context context =new Context();
        context.setVariable("email",user.getEmail());
        String url = domain+contextpath+"/activation/"+user.getId()+"/"+user.getActivationCode();
        System.out.println(url);
        context.setVariable("url",url);
        String content = templateEngine.process("/mail/activation",context);
        mailClient.sendMail(user.getEmail(),"激活账号",content);
        return map;
    }
    public int activation(int userid,String code){
        User user = userMapper.selectByid(userid);
        if(user.getStatus()==1){
            return  ACTIVATION_REPEAT;
        }
        else{
            if(user.getActivationCode().equals(code)){
                userMapper.updataStatus(userid,1);
                return ACTIVATION_SUCCESS;
            }else{
                return ACTIVATION_FAILURE;
            }
        }
    }
}
