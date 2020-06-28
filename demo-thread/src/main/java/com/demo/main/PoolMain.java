package com.demo.main;

import com.demo.pool.ThreadPool;

import java.util.concurrent.TimeUnit;

/**
 * 当你需要同时限制应用程序中运行的线程数时，线程池非常有用。 启动新线程会产生性能开销，每个线程也会为其堆栈等分配一些内存。
 *
 * 可以将任务传递给线程池，而不是为每个任务启动并发执行的新线程。 只要线程池有任何空闲线程，任务就会分配给其中一个线程并执行。 在内部，任务被插入到阻塞队列中，池中的线程从该阻塞队列中出队。 当新任务插入队列时，其中一个空闲线程将成功将其出列并执行它。 线程池中的其余空闲线程将被阻塞，等待出列任务。
 *
 * 从上述所知，一个基本的线程池需要具有
 *
 * 一个存储线程的容器（容器可以使用队列，链表等数据结构），当有任务时，就从容器中拿出一个线程，来执行任务。
 * 一个存储任务的阻塞队列。（阻塞队列可以控制任务提交的最大数）
 * 线程池对外暴露一个execute(Runnable task)方法，用以外界向线程池中提交任务。
 *
 * 作者：在下喵星人
 * 链接：https://www.jianshu.com/p/6130557eccfb
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public class PoolMain {
    public static void main(String[] args) throws Exception {

        final ThreadPool threadPool = new ThreadPool(5 , 20);

        //定义20个任务并且提交到线程池
        for (int i = 0; i < 20; i++) {
            threadPool.execute(() ->{
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName() + " is running add done");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        while (true){
            System.out.println("---------------------------------");
            TimeUnit.SECONDS.sleep(5);
        }

    }
}
