package test;

import java.util.concurrent.CountDownLatch;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 前置知识：控制两个线程执行先后顺序 方式很多 我们用CountDownLatch（这个知识点之前文章写到过）
 *    // git地址：https://github.com/githubforliming/order.git
 */
public class Test00 {

    public static void main(String[] args) throws InterruptedException {
        // 让线程2先执行输出 再执行线程1的输出
        CountDownLatch cdl = new CountDownLatch(1);
        new Thread(()->{
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+"输出");
        },"线程1").start();

        new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"输出");
            cdl.countDown();
        },"线程2").start();
    }

}
