package com.demo.pool;

import com.demo.queue.BlockingQueue;
import com.demo.thread.PoolThread;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * 新建线程池类
 */
public class ThreadPool implements Service {

    /**
     * 任务队列，用来存储提交的任务
     */
    private BlockingQueue<Runnable> taskQueue = null;

    /**
     * 线程池中存储线程的容器。
     */
    private Queue<PoolThread> threads = new ArrayDeque<PoolThread>();

    private boolean isShutdown = false;

    public ThreadPool(int initSize, int maxNoOfTasks){
        taskQueue = new BlockingQueue<Runnable>(maxNoOfTasks);

        //初始化线程池
        for (int i = 0; i < initSize; i++) {
            threads.add(new PoolThread(taskQueue));
        }

        //启动线程池线程
        threads.forEach(thread -> thread.start());
    }

    @Override
    public synchronized void execute(Runnable task)  {
        if (this.isShutdown){
            throw new IllegalStateException("ThreadPool is stopped");
        }
        //任务入列
        try {
            taskQueue.enqueue(task);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void shutdown(){
        this.isShutdown= true;
        threads.forEach(thread -> thread.doStop());
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }
}
