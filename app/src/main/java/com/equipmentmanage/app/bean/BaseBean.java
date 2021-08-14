package com.equipmentmanage.app.bean;

/**
 * @Description: 基类实体
 * @Author: zzh
 * @CreateDate: 2021/8/10
 */
public class BaseBean<T> {

    private boolean success;

    private String message;

    private int code;

    private T result;

    private int timestamp;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
}
