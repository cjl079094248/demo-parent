package com.demo.exception;

/**
 * 通用信息异常
 * 
 * @author cjl
 */
public class CommException extends RuntimeException
{
    //
    private static final long serialVersionUID = 2146840966262730160L;

    public CommException(String msg)
    {
        super(msg);
    }
}