package netease;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by geek on 2017/8/23.
 */
public class ZTE {

    public static void main(String[] args) {
        int[] a = {3,6,1,4,2,3,7,5,8};
        Stack<Integer> s = new Stack<>();
        List<Integer> list = new ArrayList<>(a.length);
        for(int i=0;i<a.length;i++){
            if(i == 0 ){
                s.push(a[i]);
                list.add(-1);
            }else{
                if(s.peek()<a[i]){
                    list.add(s.peek());
                }else{
                    s.pop();
                    s.push(a[i]);
                    list.add(-1);
                }
            }
        }
        for (int o :list){
            System.out.print(o+" ");
        }
    }
}
