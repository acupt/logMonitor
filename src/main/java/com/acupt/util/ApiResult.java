package com.acupt.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liujie on 2017/5/17.
 */
public class ApiResult {

    private boolean success;

    private String message;

    private Map<String, Object> data = new HashMap<String, Object>();

    public ApiResult() {
    }

    public ApiResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public void putData(String key, Object value) {
        data.put(key, value);
    }

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

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }
}
