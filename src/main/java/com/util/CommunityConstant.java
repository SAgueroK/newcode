package com.util;

public interface CommunityConstant {
    /**
     *
     * 激活成功
     *
    * */
    int ACTIVATION_SUCCESS=0;
    /*
    *   重复激活
    *
    * */
    int ACTIVATION_REPEAT=1;
    /*激活失败
    * */
    int ACTIVATION_FAILURE=2;
    /*
    * 默认状态对的登录凭证超时时间
    *
    * */
    int DEAFAULT_EXPIRED_SECONDS = 3600*12;
    /*
    * 记住后登录凭证时间
    *
    * */
    int REMEMBER_ME_EXPIRED_SECONDS=3600*24*100;

    /*
    * 帖子实体类型
    * */
    int ENTITY_TYPE_POST=1;
    /*
    * 评论实体类型
    * */
    int ENTITY_TYPE_COMMENT=2;
}
