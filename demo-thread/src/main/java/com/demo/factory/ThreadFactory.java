package com.demo.factory;

/**
 * 使用工厂模式有一下好处
 *
 * 对象的创建如果比较复杂，需要经过一系列的初始化。使用工厂模式，可以屏蔽这过程。
 * 把同一类事物归于一个框架之下。比如A和B，他们需要自己定义线程池线程创建，但规定他们都要实现工厂接口，便可以把他们控制在同一框架之下。
 * 解耦。(只要是不直接创建目标对象，基本上都可以叫解耦或者对修改关闭对扩展开放)
 *
 * 作者：在下喵星人
 * 链接：https://www.jianshu.com/p/6130557eccfb
 * 来源：简书
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 */
public interface ThreadFactory {
    Thread createThread(Runnable runnable);
}
