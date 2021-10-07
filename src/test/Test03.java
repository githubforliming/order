package test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式2：Lock  ReentrantLock
 *     // 关于ReentrantLock Condition相关知识以及CAS 可以关注我公众号 我都有写到相关文章
 *     // git地址：https://github.com/githubforliming/order.git
 */
public class Test03 {

    public static void main(String[] args) throws InterruptedException {
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    // 使用wait notify 之前文章写过使用方式
    public static void OrderTest(char[] nums, char[] letters) {
        CountDownLatch cdl = new CountDownLatch(1);
        // 锁 关于锁的使用
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            try {
                lock.lock();
                for (int i = 0; i < letters.length; i++) {
                    // 输出字母
                    System.out.print(letters[i]);
                    cdl.countDown();
                    try {
                        // 通知另一个线程执行输出动作
                        condition.signal();
                        // 让出锁 等别人通知我再执行(signal)
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 不要忘记unlock
                lock.unlock();
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            try {
                // 等线程1先输出
                cdl.await();
                lock.lock();
                for (int i = 0; i < nums.length; i++) {
                    // 输出字母
                    System.out.print(nums[i]);
                    try {
                        // 通知另一个线程执行输出动作
                        condition.signal();
                        // 让出锁 等别人通知我再执行(signal)
                        condition.await();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                condition.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 不要忘记unlock
                lock.unlock();
            }
        });

        // 开启线程
        thread02.start();
        thread01.start();
    }

}
