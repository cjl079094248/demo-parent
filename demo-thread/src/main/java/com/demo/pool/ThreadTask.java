package com.demo.pool;

import com.demo.thread.PoolThread;

public class ThreadTask {
    Thread thread;
    PoolThread poolThread;

    public ThreadTask(Thread thread , PoolThread poolThread){
        this.thread = thread;
        this.poolThread = poolThread;
    }
}
