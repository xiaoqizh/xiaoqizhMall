package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;

/**
 * @Author: xiaoqiZh
 * @Date: Created in 12:41 2018/3/10
 * @Description:
 *
 *      用于服务器向前端传递json序列化的pojo
 */

public class ServerResponse<T> implements Serializable {
    private  int  status;
    private  String  msg;
    private  T data;
    /**
     * 私有化构造函数
     */
    private ServerResponse (int status) {
        this.status = status;
    }

    private ServerResponse(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    private ServerResponse(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }
    private ServerResponse(int status, T data) {
        this.status = status;
        this.data = data;
    }

    /**
     * 通过静态方法获得构造函数
     */
    public static ServerResponse createBySuccess() {
        return new ServerResponse(ResponseCode.SUCCESS.getCode());
    }
    public static <T> ServerResponse createBySuccess(T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),data);
    }
    public static <T> ServerResponse createBySuccess(String  msg,T data) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(),msg,data);
    }

    public static ServerResponse createBySuccess(String msg) {
        return new ServerResponse(ResponseCode.SUCCESS.getCode(), msg);
    }
    //一般错误代码是不用返回data信息的
    public static ServerResponse createByError() {
        return new ServerResponse(ResponseCode.ERROE.getCode());
    }

    public static ServerResponse createByError(String msg) {
        return new ServerResponse(ResponseCode.ERROE.getCode(), msg);
    }

    @JsonIgnore
    public boolean isSuccess() {
        //判断该是否为正确的返回结果
        return this.status == ResponseCode.SUCCESS.getCode();
    }

    public int getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
         return data;
    }
}
