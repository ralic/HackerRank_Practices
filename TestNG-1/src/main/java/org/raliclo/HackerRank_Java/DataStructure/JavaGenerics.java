package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/java-generics

import java.lang.reflect.Method;

public class JavaGenerics {

    public static class Printer {
        //Write your code here
        public void printArray(Object[] array) {
//            Arrays.stream(array).forEach(System.out::println);
            for (int i = 0; i < array.length; i++) {
                System.out.println(array[i]);
            }
        }
    }


    public static void main(String args[]) {
        Printer myPrinter = new Printer();
        Integer[] intArray = {1, 2, 3};
        String[] stringArray = {"Hello", "World"};
        myPrinter.printArray(intArray);
        myPrinter.printArray(stringArray);
        int count = 0;

        for (Method method : Printer.class.getDeclaredMethods()) {
            String name = method.getName();

            if (name.equals("printArray"))
                count++;
        }

        if (count > 1) System.out.println("Method overloading is not allowed!");

    }
}