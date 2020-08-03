package org.tinygame.herostory.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName UserManager
 * @Description 用户管理器
 * @Author zhanghongjun
 * @Date 2020-08-03 22:01
 * @Version 1.0
 */
public final class UserManager {
    /**
     * 私有化类默认构造器
     */
    private UserManager(){}
    /**
     * 用户字典
     */
    static private final Map<Integer, User> _userMap = new HashMap<>();

    /**
     * 添加用户
     * @param newUser
     */
    static  public void add(User newUser){
        if(null!=newUser){
            _userMap.put(newUser.userId,newUser);
        }

    }

    /**
     * 根据用户ID移除用户
     * @param userId
     */
    static  public void remove(int userId){

        _userMap.remove(userId);
    }

    /**
     * 列表用户
     * @return
     */
    static public Collection<User> listUser(){
        return _userMap.values();
    }

}
