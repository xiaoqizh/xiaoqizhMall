package com.mmall.common;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 12:47 2018/3/10
 * @Description:
 *     枚举类 来获得不同的响应码
 *     现在直到枚举类的具体作用了
 */

public enum  ResponseCode {
    SUCCESS(0, "SUCCESS"),
    ERROE(1, "ERROR"),
    NEED_LOGIN(2,"NEED_LOGIN");

    private int code;
    private  String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return this.code;
    }

    public String getMsg() {
        return this.msg;
    }
}
