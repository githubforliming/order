package test;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.LockSupport;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式1： park  unpark
 */
public class Test {

    public static void main(String[] args) throws InterruptedException {
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        lockSupportTest(nums, letters);
    }

    // 使用LockSupport LockSupport之前文章写过使用方式
    public static void lockSupportTest(char[] nums, char[] letters) throws InterruptedException {
        Map<String,Thread> container = new HashMap<>();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            for (int i = 0; i < letters.length; i++) {
                // 输出字母
                System.out.print(letters[i]);
                // 给线程2发券
                LockSupport.unpark(container.get("Thread02"));
                // 等线程1给我发券 如果是最后一个就不等了 直接结束
                if (i < (letters.length-1)) {
                    LockSupport.park();
                }
            }
        });
        // 输出数字
        Thread thread02 = new Thread(() -> {
            for (int i = 0; i < nums.length; i++) {
                // 输出字母
                System.out.print(nums[i]);
                //给线程1发券
                LockSupport.unpark(container.get("Thread01"));
                // 等线程2给发券 如果是最后一个就不等了 直接结束
                if(i < (nums.length-1)){
                    LockSupport.park();
                }
            }
        });

        // 线程放到容器中
        container.put("Thread01", thread01);
        container.put("Thread02", thread02);
        // 开启线程
        thread01.start();
        Thread.sleep(1000);// 确保线程1先启动 进行park
        thread02.start();
    }

}
