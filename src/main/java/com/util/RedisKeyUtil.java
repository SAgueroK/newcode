package com.util;

public class RedisKeyUtil {
    private static final String SPLIT=":";

    private static final String PREFIX_ENTITY_LIKE="like:entity";
    private static final String PERFIX_USER_LIKE="like:user";
    //某个实体的赞
    //like：entity：entityType：eneityId ->set(userId)
    public static String getEntityLikeKey(int entityType,int entityId){
        return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
    }
    //用户的赞
    //like:user:userId ->int
    public static String getUserLikeKey(int userId){
        return PERFIX_USER_LIKE+SPLIT+userId;
    }


}
