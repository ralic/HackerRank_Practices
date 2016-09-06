/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.raliclo.HackerRank_Java.JavaOOP;/**
 * Created by raliclo on 8/20/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-instanceof-keyword

import java.util.ArrayList;
import java.util.Scanner;


class Student {
}

class Rockstar {
}

class Hacker {
}

public class JavaInstanceofKeyword {


    static String count(ArrayList mylist) {
        int a = 0, b = 0, c = 0;
        for (int i = 0; i < mylist.size(); i++) {
            Object element = mylist.get(i);
            if (element instanceof Student)
//            if (~~Complete this line ~~)
                a++;
            if (element instanceof Rockstar)
//            if (~~Complete this line ~~)
                b++;
            if (element instanceof Hacker)
//            if (~~Complete this line ~~)
                c++;
        }
//        String ret = Integer.toString(a) + " " + Integer.toString(b) + " " + Integer.toString(c);
        return a + " " + b + " " + c;
    }

    public static void main(String[] args) {
        ArrayList<Object> mylist = new ArrayList();
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();
        for (int i = 0; i < t; i++) {
            String s = sc.next();
            if (s.equals("Student")) mylist.add(new Student());
            if (s.equals("Rockstar")) mylist.add(new Rockstar());
            if (s.equals("Hacker")) mylist.add(new Hacker());
        }
        System.out.println(count(mylist));
    }
}
