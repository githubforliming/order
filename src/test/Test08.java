package test;

import java.util.concurrent.Semaphore;
/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式6：Semaphore
 *     //https://github.com/githubforliming/order.git
 */
public class Test08 {

    public static void main(String[] args){
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    public static void OrderTest(char[] nums, char[] letters){
        Semaphore semaphore01 = new Semaphore(0);
        Semaphore semaphore02 = new Semaphore(0);
        // 输出字母
        Thread thread01 = new Thread(() -> {
            try {
                for (int i = 0; i < letters.length; i++) {
                    // 等待通知准备输出
                    semaphore01.acquire();
                    // 输出
                    System.out.print(letters[i]);
                    // 通知线程2输出
                    semaphore02.release();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            try {
                for (int i = 0; i < nums.length; i++) {
                    // 通知线程1输出
                    semaphore01.release();
                    // 等着通知准备输出
                    semaphore02.acquire();
                    // 输出
                    System.out.print(nums[i]);
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
