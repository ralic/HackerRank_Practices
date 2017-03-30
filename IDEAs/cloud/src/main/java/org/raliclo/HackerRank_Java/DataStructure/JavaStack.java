package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

public class JavaStack {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        String[] spec = {"(", ")", "{", "}", "[", "]"};

        input.forEach(
                (m) -> {
                    HashMap<String, Integer> map = new HashMap<>();
                    Arrays.stream(spec).forEach((m3) -> map.put(m3, 0));
                    Arrays.stream(m.split("")).forEach(
                            (m2) -> {
//                                System.out.println(map.get(m2));
                                map.put(m2, map.get(m2) + 1);
                            }
                    );
//                    System.out.println(map);
                    System.out.println(checkmap(map));
                }
        );
    }

    public static boolean checkmap(HashMap<String, Integer> map) {
        String[] spec = {"(", ")", "{", "}", "[", "]"};
        for (int i = 0; i < spec.length; i += 2) {
            if (map.get(spec[i]) != map.get(spec[i + 1])) {
                return false;
            }
        }
        return true;
    }
}

/*
Another approach
import java.util.*;
class Solution{
   public static void main(String []argh){
      Scanner sc = new Scanner(System.in);
      boolean found = false;

    while (sc.hasNextLine()) {
        Stack myStack = new Stack();
        String input=sc.nextLine();
        //Complete the code
        int len = input.length();
        String c = "x";
        for (int i = 0; i < len; i++){
            found = false;
            c = input.substring(i, i + 1);
            if (!myStack.empty()){
                if ((c.equals(")"))&&(myStack.peek().equals("("))){
                    myStack.pop();
                    found = true;
                }

                if ((c.equals("]"))&&(myStack.peek().equals("["))){
                    myStack.pop();
                    found = true;
                }

                if ((c.equals("}"))&&(myStack.peek().equals("{"))){
                    myStack.pop();
                    found = true;
                }

                if (!found){
                    myStack.push(c);}
            } else{
                myStack.push(c);}
        }

        if (myStack.empty()){System.out.println("true");
        }else{System.out.println("false");}
    }

   }
}
 */