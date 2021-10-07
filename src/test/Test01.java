package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.LockSupport;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式1： park  unpark
 *     // git地址：https://github.com/githubforliming/order.git
 */
public class Test01 {

    public static void main(String[] args) throws InterruptedException {
        // 定义数组
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    // 使用LockSupport LockSupport之前文章写过使用方式
    public static void OrderTest(char[] nums, char[] letters)   {
        // 控制线程执行顺序
        CountDownLatch cdl = new CountDownLatch(1);
        // 存储线程
        Map<String,Thread> container = new HashMap<>();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            for (int i = 0; i < letters.length; i++) {
                // 输出字母
                System.out.print(letters[i]);
                cdl.countDown();
                // 等线程2通知（unpark）
                LockSupport.park();
                // 通知线程2执行输出
                LockSupport.unpark(container.get("Thread02"));
            }
            LockSupport.unpark(container.get("Thread02"));
        });
        // 输出数字
        Thread thread02 = new Thread(() -> {
            // 等线程1先执行输出 然后再执行线程2
            try {
                cdl.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < nums.length; i++) {
                // 输出数字
                System.out.print(nums[i]);
                //给线程1发券
                LockSupport.unpark(container.get("Thread01"));
                // 等线程2给发券
                LockSupport.park();
            }
            LockSupport.unpark(container.get("Thread01"));
        });

        // 线程放到容器中
        container.put("Thread01", thread01);
        container.put("Thread02", thread02);
        // 开启线程
        thread01.start();
        thread02.start();
    }

}
