package com.controller;

import com.annotation.LoginRequired;
import com.entity.User;
import com.service.UserSevice;
import com.util.HostHolder;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserSevice userSevice;
    @Value("${community.path.upload}")
    private String uploadPath;
    @Value("${community.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String context;

    @Autowired
    private HostHolder hostHolder;
    @LoginRequired
    @RequestMapping(path = "/setting",method = RequestMethod.GET)
    public String getSettingPage(){
        return "/site/setting";
    }
    @LoginRequired
    @RequestMapping(path = "/upload",method = RequestMethod.POST)
    public String uploadHeader(MultipartFile headerImage, Model model){
        if(headerImage==null){
            model.addAttribute("error","还没有选择图片");
            return "/site/setting";
        }
        String fileName=headerImage.getOriginalFilename();
        String suffix=fileName.substring(fileName.lastIndexOf("."));
        if(StringUtils.isEmpty(suffix)){
            model.addAttribute("error","图片格式不正确");
            return "/site/setting";
        }
        //生成随机文件名
        fileName=RandomUtil.generateUUID()+suffix;
        //确定文件存放路径
        File file =new File(uploadPath+"/"+fileName);
        try {
            //存储文件
            headerImage.transferTo(file);
        } catch (IOException e) {
            throw new RuntimeException("文件上传失败",e);
        }
        //跟新用户头像路径 http:localhost:8080/newcode/user/header/***.png
        User user =hostHolder.getUsers();
        String headUrl = domain+context+"/user/header/"+fileName;
        userSevice.updataHeader(user.getId(),headUrl);
        return "redirect:/index";
    }
    @RequestMapping(path = "/header/{filename}",method = RequestMethod.GET)
    public void getHeader(@PathVariable("filename") String fileName, HttpServletResponse response){
        //服务器存放的路径
        fileName=uploadPath+"/"+fileName;
        String suffix = fileName.substring(fileName.lastIndexOf(".")+1);
        response.setContentType("image/"+suffix);

        try (
            FileInputStream fis = new FileInputStream(fileName);
            OutputStream out =response.getOutputStream();
        ){
            byte[] buffer =new byte[1024];
            int b= 0;
            while((b=fis.read(buffer))!=-1){
                out.write(buffer,0,b);
            }

        } catch (IOException e) {
            throw new RuntimeException("加载图片失败");
        }
    }
}
