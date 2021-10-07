package test;

import java.util.concurrent.CountDownLatch;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式2：synchronized wait notify
 */
public class Test06 {

    public static void main(String[] args) throws InterruptedException {
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        // 死锁了吗？？
        for (int i = 0; i < 50; i++) {
            String res = WaitNotifyTest(nums, letters);
            String want = "A1B2C3D4E5F6G7";
            if (!want.equals(res)) {
                System.out.println("错误的输出！--> "+res);
            }
        }
    }



    // 使用wait notify 之前文章写过使用方式
    public static String WaitNotifyTest(char[] nums, char[] letters) throws InterruptedException {
        MyBoolean myBoolean = new MyBoolean();
        // 两个线程执行完后 方法再返回
        CountDownLatch cdl = new CountDownLatch(2);
        // 锁
        Object lock = new Object();
        // 线程安全的
        StringBuffer sbuilder = new StringBuffer();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            // 争夺锁 这里一定注意是先synchronized再执行for wait notify 
            synchronized (lock) {
                for (int i = 0; i < letters.length; i++) {
                    // 输出
                    sbuilder.append(letters[i]);
                    myBoolean.isStart = true;
                    try {
                        // 通知另一个线程执行输出动作
                        lock.notify();
                        // 让出锁 等别人通知我再执行
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 防止最后线程wait没人notify
                lock.notify();
            }
            cdl.countDown();
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            // 争夺锁
            synchronized (lock) {
                // 上来就wait 这块可能不对 如果线程1执行完输出 走到了wait这里刚进来执行wait就没人notify了
/*                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }*/
                //
                if (!myBoolean.isStart) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < nums.length; i++) {
                    // 输出
                    sbuilder.append(nums[i]);
                    try {
                        // 通知另一个线程执行输出动作
                        lock.notify();
                        // 让出锁 等别人通知我再执行
                        lock.wait();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                // 防止最后线程wait没人notify
                lock.notify();
            }
            cdl.countDown();
        });

        // 开启线程
        thread02.start();
        thread01.start();

        cdl.await();
        return sbuilder.toString();
    }

}

// 自定义类 每次执行WaitNotifyTest的时候新建一个MyBoolean 然后2个线程共享
// 不能用Test06的静态变量 容易出问题（多个线程共享）
class MyBoolean{
    boolean isStart = false;
}
