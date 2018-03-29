package com.mmall.common;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 15:39 2018/3/10
 * @Description:
 *
 *    本类用于存放一些魔法值
 *
 */

public class Const {
    public static final String CURRNET_USER = "currentUser";

    public interface Role {
        public int USER_CUSTOMER = 0;
        public int USER_ADMIN = 1;
    }

    public interface TYPE {
        public String EMAIL = "email";
        public String USERNAME = "username";
    }
}