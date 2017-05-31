package com.quizflix.dao;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kavasthi on 4/11/2016.
 */
public class BaseResponse<T> {
    @SerializedName("Message")
    private String message;
    @SerializedName("Success")
    private boolean success;
    private T data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
