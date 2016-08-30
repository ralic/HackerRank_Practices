package org.raliclo.HackerRank_Java.JavaOOP;/**
 * Created by raliclo on 8/20/16.
 * Project Name : TestNG-1
 */

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class JavaIterator {

    static Iterator func(ArrayList mylist) {
        Iterator it = mylist.iterator();
        while (it.hasNext()) {
//                Object element = ~~~Complete this line~~~
            Object element = it.next();
            if (element instanceof Integer==false)
//                if(~~~Complete this line~~~)//Hints: use instanceof operator
                break;
        }
        return it;

    }

    public static void main(String[] args) {
        ArrayList mylist = new ArrayList();
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        for (int i = 0; i < n; i++) {
            mylist.add(sc.nextInt());
        }

        mylist.add("###");
        for (int i = 0; i < m; i++) {
            mylist.add(sc.next());
        }

        Iterator it = func(mylist);
        while (it.hasNext()) {
            Object element = it.next();
            System.out.println( element);
        }
    }
}
