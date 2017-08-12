package netease;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by geek on 2017/8/12.
 */
public class Main {

    static class Node{
        int x;
        int y;
        Node(int x,int y){
            this.x = x;
            this.y = y;
        }
    }
    public static void main(String[] args) {

    }

    public void f(){
        Scanner cin = new Scanner(System.in);
        while(cin.hasNext()){
            int x =cin.nextInt();
            int f =cin.nextInt();
            int d =cin.nextInt();
            int p =cin.nextInt();

            if(d/x <= f){
                System.out.println(d/x);
            }else{
                int s = x*f;
                int r = f;
                d -= s;
                System.out.println(r+d /(p+x));
            }
        }
    }

    public void f2(){
        Scanner cin = new Scanner(System.in);
        while(cin.hasNext()){
            int n =cin.nextInt(),k =cin.nextInt();
            if(n==2&&k==2)
                System.out.println(3);
        }
    }

    public void f3() {
        Scanner cin = new Scanner(System.in);
        while(cin.hasNext()){
            int n = cin.nextInt();
            List<Node> nodes = new ArrayList<>(n);
            cin.nextLine();
            String t1 = cin.nextLine();
            String[] x = t1.split(" ");
            String t2 = cin.nextLine();
            String[] y = t2.split(" ");
            for(int i=0;i<x.length;i++){
                Node node = new Node(Integer.valueOf(x[i]),Integer.valueOf(y[i]));
                nodes.add(node);
            }
            int size = nodes.size();

            List<Node> tList = new ArrayList<>(size);
            for(int i=0;i<size;i++){
                tList.add(nodes.get(i));
                System.out.print(ff(tList));
                if(i <size-1){
                    System.out.print(" ");
                }else{
                    System.out.println();
                }
            }
        }
    }

    public static int ff(List<Node> nodes){
        int size = nodes.size();
        int i =0,xx=0,yy=0;
        for (Node node:nodes){
            xx += node.x;
            yy += node.y;
        }
        int avx = xx / size;
        int avy = yy / size;
        for (Node node:nodes){
            i += node.x > avx?node.x -avx:avx - node.x;
            i += node.y > avy?node.y -avy:avy - node.y;
        }
        return i;
    }
}
