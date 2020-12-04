package com.demo.utils;


import com.demo.vo.ResultApp;

/**
 * @author cjl
 */
public class ResultUtilApp<T> {

    private ResultApp<T> result;

    public ResultUtilApp(){
        result = new ResultApp<>();
        result.setMsg("success");
        result.setCode(200);
    }

    public ResultApp<T> setData(T t){
        this.result.setData(t);
        this.result.setCode(200);
        return this.result;
    }

    public ResultApp<T> setSuccessMsg(String msg){
        this.result.setMsg(msg);
        this.result.setCode(200);
        this.result.setData(null);
        return this.result;
    }

    public ResultApp<T> setData(T t, String msg){
        this.result.setData(t);
        this.result.setCode(200);
        this.result.setMsg(msg);
        return this.result;
    }

    public ResultApp<T> setErrorMsg(String msg){
        this.result.setMsg(msg);
        this.result.setCode(500);
        return this.result;
    }

    public ResultApp<T> setErrorMsg(Integer code, String msg){
        this.result.setMsg(msg);
        this.result.setCode(code);
        return this.result;
    }

    public static <T> ResultApp<T> data(T t){
        return new ResultUtilApp<T>().setData(t);
    }

    public static <T> ResultApp<T> data(T t, String msg){
        return new ResultUtilApp<T>().setData(t, msg);
    }

    public static <T> ResultApp<T> success(String msg){
        return new ResultUtilApp<T>().setSuccessMsg(msg);
    }

    public static <T> ResultApp<T> error(String msg){
        return new ResultUtilApp<T>().setErrorMsg(msg);
    }

    public static <T> ResultApp<T> error(Integer code, String msg){
        return new ResultUtilApp<T>().setErrorMsg(code, msg);
    }
}