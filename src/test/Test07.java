package test;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式5：BlockingQueue
 *     //https://github.com/githubforliming/order.git
 */
public class Test07 {

    public static void main(String[] args){
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    // 使用wait notify 之前文章写过使用方式
    public static void OrderTest(char[] nums, char[] letters){
        BlockingQueue bqletters = new ArrayBlockingQueue(1);
        BlockingQueue bqnums = new ArrayBlockingQueue(1);
        // 输出字母
        Thread thread01 = new Thread(() -> {
            // 争夺锁 这里一定注意是先synchronized再执行for wait notify 
            for (int i = 0; i < letters.length; i++) {
                try {
                    // 字母放入queue
                    bqletters.put(letters[i]);
                    // 阻塞获取数字 准备输出
                    Object take = bqnums.take();
                    System.out.print(take);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            for (int i = 0; i < nums.length; i++) {
                try {
                    // 阻塞获取
                    Object take = bqletters.take();
                    // 输出
                    System.out.print(take);
                    // 数字放入queue
                    bqnums.put(nums[i]);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        // 开启线程
        thread02.start();
        thread01.start();
    }

}
