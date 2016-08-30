package org.raliclo.HackerRank_Java.JavaOOP;

/**
 * Created by raliclo on 8/20/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-method-overriding-2-super-keyword

class BiCycle {
    String define_me() {
        return "a vehicle with pedals.";
    }
}

class MotorCycle extends BiCycle {
    String define_me() {
        return "a cycle with an engine.";
    }

    MotorCycle() {
        System.out.println("Hello I am a motorcycle, I am " + define_me());
//        String temp = define_me(); //Fix this line
        String temp=super.define_me();
        System.out.println("My ancestor is a cycle who is " + temp);
    }

}

public class JavaMethodOverriding2_Super_Keyword {
    public static void main(String[] args) {
        MotorCycle M = new MotorCycle();
    }
}
