package com.demo.thread;

import com.demo.queue.BlockingQueue;

/**
 * 新建一个线程池线程类，用来执行提交的任务。结构体中传入任务队列，run()方中发现taskQueue有任务时，获取任务并执行，没有任务就阻塞。
 */
public class PoolThread extends Thread {


    private BlockingQueue taskQueue = null;

    private boolean isStopped = false;

    public PoolThread(BlockingQueue taskQueue) {
        this.taskQueue = taskQueue;
    }

    public void run(){
        while(!isStopped() && !Thread.currentThread().isInterrupted()){
            try{
                //从任务队列获取任务并执行
                Runnable runnable = (Runnable) taskQueue.dequeue();
                runnable.run();
            } catch(Exception e){
                isStopped = true;
                break;
            }
        }
    }

    public synchronized void doStop(){
        isStopped = true;
        this.interrupt();
    }

    public synchronized boolean isStopped(){
        return isStopped;
    }
}
