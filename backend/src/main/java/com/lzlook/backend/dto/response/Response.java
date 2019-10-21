package com.lzlook.backend.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(value= JsonInclude.Include.NON_NULL)
public class Response {

    private String msg;

    private int code;

    public Response() {
    }

    public Response(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public static final int PARAM_INVALID = -10;

    public static final int NOT_LOGIN = -20;

    public static final int FAILED_WITH_INFO = -51;

    public static final int FAILED = -50;

    public static final int SUCCESS = 200;

    public static final int CODE_UNAUTHORIZED = -1;

    protected void success() {
        this.code = Response.SUCCESS;
    }

    protected void fail(String msg) {
        this.code = Response.FAILED_WITH_INFO;
        this.msg = msg;
    }

    protected void fail() {
        this.code = Response.FAILED;
    }
}

