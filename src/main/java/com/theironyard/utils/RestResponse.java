package com.theironyard.utils;

/**
 * Created by ahanger on 5/15/2016.
 */
public class RestResponse {

    private String SUCCESS_MESSAGE;
    private String SUCCESS_CODE;

    public RestResponse(String SUCCESS_MESSAGE, String SUCCESS_CODE) {
        this.SUCCESS_MESSAGE = SUCCESS_MESSAGE;
        this.SUCCESS_CODE = SUCCESS_CODE;
    }

    public RestResponse() {

    }

    public String getSUCCESS_MESSAGE() {
        return SUCCESS_MESSAGE;
    }

    public void setSUCCESS_MESSAGE(String SUCCESS_MESSAGE) {
        this.SUCCESS_MESSAGE = SUCCESS_MESSAGE;
    }

    public String getSUCCESS_CODE() {
        return SUCCESS_CODE;
    }

    public void setSUCCESS_CODE(String SUCCESS_CODE) {
        this.SUCCESS_CODE = SUCCESS_CODE;
    }
}
