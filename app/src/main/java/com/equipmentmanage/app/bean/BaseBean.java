package com.equipmentmanage.app.bean;

/**
 * @Description: 基类实体
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class BaseBean<T> {
    /**
     * error : false
     */
    public boolean Success;

    public int Count;

    public int Code;

    public String Message;

    public T Data;

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean success) {
        Success = success;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public T getData() {
        return Data;
    }

    public void setData(T data) {
        Data = data;
    }
}
