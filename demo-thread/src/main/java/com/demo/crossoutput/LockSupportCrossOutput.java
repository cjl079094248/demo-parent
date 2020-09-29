package com.demo.crossoutput;

import java.util.concurrent.locks.LockSupport;

/**
 * 使用LockSupport来控制交叉输出
 * @author cjl
 */
public class LockSupportCrossOutput {
    public static Thread t1 = null;
    public static Thread t2 = null;

    public static void main(String[] args) {
        char[] num = "1234567".toCharArray();
        char[] str = "ABCDEFG".toCharArray();

        t1 = new Thread(()->{
            for(char n : num){
                LockSupport.park();
                System.out.println(n);
                LockSupport.unpark(t2);
            }
        },"t1");

        t2 = new Thread(()->{
            for(char s : str){
                System.out.println(s);
                LockSupport.unpark(t1);
                LockSupport.park();
            }
        },"t2");

        t1.start();
        t2.start();

    }
}
