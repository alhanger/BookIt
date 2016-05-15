package com.theironyard.utils;

/**
 * Created by ahanger on 5/15/2016.
 */
public class RestResponse {

    private String message;
    private String code;

    public RestResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public RestResponse() {

    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
