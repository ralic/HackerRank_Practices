package org.raliclo.HackerRank_Java.JavaOOP;

/**
 * Created by raliclo on 7/26/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-inheritance-1
class Animal {
    void walk() {
        System.out.println("I am walking");
    }
}


class Bird extends Animal {
    void fly() {
        System.out.println("I am flying");
    }

    void sing() {
        System.out.println("I am singing");
    }
}

public class JavaInheritanceI {

    public static void main(String args[]) {

        Bird bird = new Bird();
        bird.walk();
        bird.fly();
        bird.sing();

    }
}