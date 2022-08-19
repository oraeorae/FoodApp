package com.coding.demo.model;

import lombok.Data;

@Data
public class JsonResult<T> {

    private T data;
    private String code;
    private String msg;
    private String status;

    public JsonResult(T data){
        this.data = data;
        this.code = "200";
        this.msg = "success";
        this.status = "success";
    }
    public JsonResult(T data, String code, String msg){
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.status = "fail";
    }
    public JsonResult(T data, String code, String msg,String status){
        this.data = data;
        this.code = code;
        this.msg = msg;
        this.status = status;
    }

}
