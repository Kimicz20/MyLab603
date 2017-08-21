package netease;

import java.util.*;

/**
 * Created by geek on 2017/8/21.
 */
public class Ali {
/** 请完成下面这个函数，实现题目要求的功能 **/
    /**
     * 当然，你也可以不按照这个模板来作答，完全按照自己的想法来 ^-^
     **/
    static int pick(int[] peaches) {
        int n = peaches.length;
        float[] B = new float[n + 1];//数组B；
        B[0] = -10000;//把B[0]设为最小，假设任何输入都大于-10000；
        B[1] = peaches[0];//初始时，最大递增子序列长度为1的最末元素为a1
        int Len = 1;//Len为当前最大递增子序列长度，初始化为1；
        int p, r, m;//p,r,m分别为二分查找的上界，下界和中点；
        for (int i = 1; i < n; i++) {
            p = 0;
            r = Len;
            while (p <= r)//二分查找最末元素小于ai+1的长度最大的最大递增子序列；
            {
                m = (p + r) / 2;
                if (B[m] < peaches[i]) p = m + 1;
                else r = m - 1;
            }
            B[p] = peaches[i];//将长度为p的最大递增子序列的当前最末元素置为ai+1;
            if (p > Len) Len++;//更新当前最大递增子序列长度；


        }
        return Len;
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int trees = Integer.parseInt(in.nextLine().trim());
        int[] peaches = new int[trees];
        for (int i = 0; i < peaches.length; i++) {
            peaches[i] = Integer.parseInt(in.nextLine().trim());
        }
        System.out.println(pick(peaches));
    }
}
