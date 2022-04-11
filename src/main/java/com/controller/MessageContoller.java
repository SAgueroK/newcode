package com.controller;

import com.annotation.LoginRequired;
import com.entity.Message;
import com.entity.Page;
import com.entity.User;
import com.service.MessageService;
import com.service.UserSevice;
import com.util.HostHolder;
import com.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class MessageContoller {
    @Autowired
    private MessageService messageService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserSevice userSevice;
    //私信列表
    @LoginRequired
    @RequestMapping(path = "/letter/list",method = RequestMethod.GET)
    public String getLetterList(Model model, Page page){
        User user = hostHolder.getUser();
        //设置分页
        page.setLimit(5);
        page.setPath("/letter/list");
        page.setRows(messageService.findConversationCount(user.getId()));
        //会话列表
        List<Message> conversationsList=messageService.findConversations(user.getId(),page.getOffset(),page.getLimit());
        List<Map<String,Object>> conversations = new ArrayList<>();
        if(conversationsList!=null){
            for(Message message:conversationsList){
                Map<String,Object> map =new HashMap<>();
                map.put("conversation",message);
                map.put("unreadCount",messageService.findLetterUnreadCount(user.getId(),message.getConversationId()));
                map.put("letterCount",messageService.findLetterCount(message.getConversationId()));
                int targetId = user.getId()==message.getFromId()?message.getToId():message.getFromId();
                map.put("target",userSevice.findUserById(targetId));
                conversations.add(map);
            }
        }
        model.addAttribute("conversations",conversations);

        //查询未读消息数量
        int letterUnreadCount = messageService.findLetterUnreadCount(user.getId(),null);
        model.addAttribute("letterUnreadCount",letterUnreadCount);

        return "/site/letter";
    }
    @LoginRequired
    @RequestMapping(path = "/letter/detail/{conversationId}",method = RequestMethod.GET)
    public String getLetterDetail(@PathVariable("conversationId") String conversationId,Page page,Model model){
        //分页设置
        page.setLimit(5);
        page.setPath("/letter/detail/"+conversationId);
        page.setRows(messageService.findLetterCount(conversationId));
        //私信列表
        List<Message> messageList=messageService.findLetters(conversationId,page.getOffset(),page.getLimit());
        List<Map<String,Object>> letters = new ArrayList<>();
        if(messageList!=null){
            for(Message message:messageList){
                Map<String ,Object> map = new HashMap<>();
                map.put("letter",message);
                map.put("fromUser",userSevice.findUserById(message.getFromId()));
                letters.add(map);
            }
        }
        model.addAttribute("letters",letters);
        //私信目标
        model.addAttribute("target",getLetterTarget(conversationId));
        //消息未读该已读
        List<Integer> ids= getLetterIds(messageList);
        if(!ids.isEmpty()){
            messageService.readMessage(ids);
        }
        return "/site/letter-detail";
    }
    private List<Integer> getLetterIds(List<Message> letterList){
        List<Integer> ids = new ArrayList<>();
        if(letterList!=null){
            for(Message message:letterList){
                if(hostHolder.getUser().getId()==message.getToId()&&message.getStatus()==1){
                    ids.add(message.getId());
                }
            }
        }
        return ids;
    }
    private User getLetterTarget(String conversationId){
        String[] ids =conversationId.split("_");
        int id0=Integer.parseInt(ids[0]);
        int id1=Integer.parseInt(ids[1]);
        if(hostHolder.getUser().getId()==id0){
            return userSevice.findUserById(id1);
        }else return userSevice.findUserById(id0);
    }
    @LoginRequired
    @RequestMapping(path = "/letter/send",method = RequestMethod.POST)
    @ResponseBody
    public String sendLetter(String toName,String content){
        User target=userSevice.findUserByName(toName);
        if(target==null){
            return RandomUtil.getJsonString(1,"目标用户不存在");

        }
        Message message = new Message();
        message.setFromId(hostHolder.getUser().getId());
        message.setToId(target.getId());
        if(message.getFromId()<message.getToId()){
            message.setConversationId(message.getFromId()+"_"+message.getToId());
        }else {
            message.setConversationId(message.getToId()+"_"+message.getFromId());
        }
        message.setContent(content);
        message.setStatus(1);
        message.setCreateTime(new Date());
        messageService.addMessage(message);
        return RandomUtil.getJsonString(0);
    }








}
