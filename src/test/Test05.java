package test;

/**
 *      @author 关注公众号：木子的昼夜编程
 *     // 面试题：2个线程交替输出A1B2C3D4E5F6G7
 *     // 方式4：自旋 对于前几种来说 这种比较耗费CPU
 */
public class Test05 {

    public static void main(String[] args){
        char[] nums = {'1','2','3','4','5','6','7'};
        char[] letters = {'A','B','C','D','E','F','G'};
        OrderTest(nums, letters);
    }

    static volatile WhichThread wt  = WhichThread.T1;
    public static void OrderTest(char[] nums, char[] letters){
        // 输出字母
        Thread thread01 = new Thread(() -> {
            for (int i = 0; i < letters.length; i++) {
                // 如果不是T1 就自旋
                while (!WhichThread.T1.equals(wt));
                // 输出
                System.out.print(letters[i]);
                // 设置为T2 让线程2输出
                wt = WhichThread.T2;
            }
        });

        // 输出数字
        Thread thread02 = new Thread(() -> {
            for (int i = 0; i < nums.length; i++) {
                // 如果不是T2 就自旋
                while (!WhichThread.T2.equals(wt));
                // 输出
                System.out.print(nums[i]);
                // 设置为T1 让线程1输出
                wt = WhichThread.T1;
            }
        });

        // 开启线程
        thread02.start();
        thread01.start();
    }

}

enum WhichThread{
    T1,T2;
}
