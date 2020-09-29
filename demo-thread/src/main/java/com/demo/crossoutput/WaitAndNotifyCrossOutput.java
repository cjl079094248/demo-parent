package com.demo.crossoutput;

/**
 * wait和notify来实现交叉打印
 */
public class WaitAndNotifyCrossOutput {
    public static void main(String[] args) {
        final Object o = new Object();
        char[] aI="1234567".toCharArray();
        char[] aC="ABCDEFG".toCharArray();

        new Thread(()->{
            synchronized (o){
                for (char c : aI){
                    try {
                        System.out.println(c);
                        o.wait();//等待资源并释放锁
                        o.notify();
                    }catch (Exception e ){
                        e.printStackTrace();
                    }
                }
            }
        },"t1").start();

        new Thread(()->{
            synchronized (o){
                for (char c :aC) {
                    System.out.println(c);
                    o.notify();
                    try{
                        o.wait();//等待资源并释放锁
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                o.notify();
            }
        },"t2").start();

    }
}
