package com.dao;

import com.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    //查询当前用户的会话列表，针对每个对话值返回一条最新消息
    List<Message> selectConversations(int userId, int offset, int limit);
    //查询当前用户的会话数量
    int selectConversationCount(int userId);
    //查询某个会话的包含的私信列表
    List<Message> selectLetters(String conversationId,int offset,int limit);

    //查询某个对话的私信数量
    int selectLetterCount(String conversationId);

    //查询未读私信的数量
    int selectLetterUnreadCount(int userId,String conversationId);
    //新增消息
    int insertMessage(Message message);
    //修改消息状态
    int updateStatus(List<Integer> ids,int status);


}
