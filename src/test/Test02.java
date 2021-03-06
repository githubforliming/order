package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式2：synchronized wait notify
 *     //关于synchronized wait notify的使用 关注我公众号包括使用以及锁升级都有写到
 *     // git地址：https://github.com/githubforliming/order.git
 */
public class Test02 {

    public static void main(String[] args) throws InterruptedException {
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    // 使用wait notify 之前文章写过使用方式
    public static void OrderTest(char[] nums, char[] letters) {
        CountDownLatch cdl = new CountDownLatch(1);
        // 锁
        Object lock = new Object();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            // 争夺锁
            synchronized (lock) {
                for (int i = 0; i < letters.length; i++) {
                    // 输出字母
                    System.out.print(letters[i]);
                    cdl.countDown();
                    try {
                        // 通知另一个线程执行输出动作
                        lock.notify();
                        // 让出锁 等别人通知我再执行(notify)
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            // 争夺锁
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            synchronized (lock) {
                for (int i = 0; i < nums.length; i++) {
                    // 输出
                    System.out.print(nums[i]);
                    try {
                        // 通知另一个线程执行输出动作
                        lock.notify();
                        // 让出锁 等别人通知我再执行(notify)
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                lock.notify();
            }
        });

        // 开启线程
        thread02.start();
        thread01.start();
    }

}
