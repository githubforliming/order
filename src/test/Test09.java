package test;

import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.TransferQueue;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式6：TrasferQueue TrasferQueue之前没有讲到过 公众号以后文章会写
 *     //https://github.com/githubforliming/order.git
 */
public class Test09 {

    public static void main(String[] args){
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    public static void OrderTest(char[] nums, char[] letters){
        TransferQueue tq = new LinkedTransferQueue();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            try {
                for (int i = 0; i < letters.length; i++) {
                    // 生产字母元素 阻塞等消费
                    tq.transfer(letters[i]);
                    // 阻塞等待其他线程生产元素
                    Object take = tq.take();
                    // 输出
                    System.out.print(take);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            try {
                for (int i = 0; i < nums.length; i++) {
                    // 阻塞等待其他线程生产元素
                    Object take = tq.take();
                    // 输出
                    System.out.print(take);
                    // 生产数字元素给消费者
                    tq.transfer(nums[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 开启线程
        thread02.start();
        thread01.start();
    }

}
