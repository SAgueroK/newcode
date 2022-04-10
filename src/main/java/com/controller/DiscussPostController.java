package com.controller;

import com.dao.DiscussPostMapper;
import com.entity.Comment;
import com.entity.DiscussPost;
import com.entity.Page;
import com.entity.User;
import com.service.CommentService;
import com.service.DiscussPostSevice;
import com.service.UserSevice;
import com.util.CommunityConstant;
import com.util.HostHolder;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.PublicKey;
import java.util.*;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController implements CommunityConstant {
    @Autowired
    private DiscussPostSevice sevice;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserSevice userSevice;
    @Autowired
    private CommentService commentService;
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
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page){
        //帖子
        DiscussPost discussPost=sevice.findDiscussPostById(discussPostId);
        model.addAttribute("post",discussPost);
        //作者
        User user = userSevice.findUserById(discussPost.getUserId());
        model.addAttribute("user",user);
        //评论
        page.setLimit(5);
        page.setPath("/discuss/detail/"+discussPostId);
        page.setRows(discussPost.getCommentCount());
        //帖子的评论
        List<Comment> commentList=commentService.findCommentsByEntity(ENTITY_TYPE_POST,discussPostId,page.getOffset(),page.getLimit());
        //帖子评论的显示列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();
        if(commentList!=null){
            for(Comment comment:commentList){
                System.out.println(comment);
                Map<String ,Object> commentVo = new HashMap<>();
                commentVo.put("comment",comment);
                commentVo.put("user",userSevice.findUserById(comment.getUserId()));
                //回复列表
                List<Comment> replyList=commentService.findCommentsByEntity(ENTITY_TYPE_COMMENT,comment.getId(),0,Integer.MAX_VALUE);
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if(replyList!=null){
                    for(Comment reply:replyList){
                        Map<String,Object> replyVo =new HashMap<>();
                        //回复
                        replyVo.put("reply",reply);
                        //作者
                        replyVo.put("user",userSevice.findUserById(reply.getUserId()));
                        //回复的目标
                        User targetUser=reply.getTargetId()==0?null:userSevice.findUserById(reply.getTargetId());
                        replyVo.put("target",targetUser);
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);
                commentVo.put("replyCount",commentService.findCommentCount(ENTITY_TYPE_COMMENT,comment.getId()));
                commentVoList.add(commentVo);
            }

        }
        model.addAttribute("comments",commentVoList);
        return "/site/discuss-detail";
    }



}
