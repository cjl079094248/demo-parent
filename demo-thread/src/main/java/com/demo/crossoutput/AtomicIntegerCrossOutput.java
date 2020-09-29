package com.demo.crossoutput;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 * 使用AtomicInteger原子类实现交叉打印
 * @author cjl
 */
public class AtomicIntegerCrossOutput {
    public static Thread t1 = null;
    public static Thread t2 = null;
    //定义一个原子性的对象
    static AtomicInteger thredNo=new AtomicInteger(2);

    public static void main(String[] args) {
        char[] num = "1234567".toCharArray();
        char[] str = "ABCDEFG".toCharArray();

        t1 = new Thread(()->{
            for(char n : num){
                while(thredNo.get() != 1){};
                System.out.println(n);
                thredNo.set(2);
            }
        },"t1");

        t2 = new Thread(()->{
            for(char s : str){
                while(thredNo.get() != 2){};
                System.out.println(s);
                thredNo.set(1);
            }
        },"t2");

        t1.start();
        t2.start();
    }
}
