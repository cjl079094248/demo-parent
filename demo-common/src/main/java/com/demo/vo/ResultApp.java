package com.demo.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author cjl
 * 前后端交互数据标准
 */

@Data
public class ResultApp<T> implements Serializable {


    private static final long serialVersionUID = 1L;

    /**
     * 消息
     */
    private String msg;

    /**
     * 返回代码
     */
    private Integer code;

    /**
     * 时间戳
     */
    private long timestamp = System.currentTimeMillis();

    /**
     * 结果对象
     */
    private T data;
}