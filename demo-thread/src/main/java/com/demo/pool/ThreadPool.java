package com.demo.pool;

import com.demo.factory.DefaultThreadFactory;
import com.demo.factory.ThreadFactory;
import com.demo.queue.BlockingQueue;
import com.demo.thread.PoolThread;

import java.util.ArrayDeque;
import java.util.Queue;

public class ThreadPool  implements Service {

    /**
     * 任务队列，用来存储提交的任务
     */
    private BlockingQueue<Runnable> taskQueue = null;

    /**
     * 线程池中存储线程的容器。
     */
    private Queue<ThreadTask> threads = new ArrayDeque<ThreadTask>();


    /**
     * 默认线程工厂
     */
    private static final ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();

    private boolean isShutdown = false;


    public ThreadPool(int initSize, int maxNoOfTasks){
        taskQueue = new BlockingQueue<Runnable>(maxNoOfTasks);

        //初始化线程池
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    private void newThread(){
        PoolThread poolThread = new PoolThread(taskQueue);
        Thread thread = DEFAULT_THREAD_FACTORY.createThread(poolThread);
        ThreadTask threadTask = new ThreadTask(thread , poolThread);
        threads.add(threadTask);
        thread.start();
    }

    @Override
    public synchronized void execute(Runnable task) throws Exception {
        if (this.isShutdown){
            throw new IllegalStateException("ThreadPool is stopped");
        }
        //任务入列
        taskQueue.enqueue(task);
    }

    @Override
    public synchronized void shutdown(){
        this.isShutdown = true;
        threads.forEach(threadTask -> threadTask.poolThread.doStop());
    }

    @Override
    public boolean isShutdown() {
        return isShutdown;
    }
}
