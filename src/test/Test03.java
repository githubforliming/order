package test;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式2：synchronized wait notify
 */
public class Test03 {

    public static void main(String[] args) throws InterruptedException {
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        WaitNotifyTest(nums, letters);
    }

    // 使用wait notify 之前文章写过使用方式
    public static void WaitNotifyTest(char[] nums, char[] letters) throws InterruptedException {
        // 锁
        Object lock = new Object();
        // 输出字母
        Thread thread01 = new Thread(() -> {
            // 争夺锁 这里一定注意是先synchronized再执行for wait notify
            synchronized (lock) {
                for (int i = 0; i < letters.length; i++) {
                    // 输出
                    System.out.print(letters[i]);
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
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            // 争夺锁
            synchronized (lock) {
                for (int i = 0; i < nums.length; i++) {
                    // 输出
                    System.out.print(nums[i]);
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
        });

        // 开启线程
        thread01.start();
        Thread.sleep(1000);// 确保线程1先启动
        thread02.start();
    }

}
