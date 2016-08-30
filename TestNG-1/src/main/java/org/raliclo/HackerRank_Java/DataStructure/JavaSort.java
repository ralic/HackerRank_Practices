package org.raliclo.HackerRank_Java.DataStructure;/**
 * Created by raliclo on 8/4/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-sort

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private int id;
    private String fname;
    private double cgpa;

    public Student(int id, String fname, double cgpa) {
        super();
        this.id = id;
        this.fname = fname;
        this.cgpa = cgpa;
    }

    public int getId() {
        return id;
    }

    public String getFname() {
        return fname;
    }

    public double getCgpa() {
        return cgpa;
    }
}

//Complete the code

public class JavaSort {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());

        ArrayList<Student> studentList = new ArrayList<>();
        while (testCases > 0) {
            int id = in.nextInt();
            String fname = in.next();
            double cgpa = in.nextDouble();

            Student st = new Student(id, fname, cgpa);
            studentList.add(st);
            studentList.sort((a, b) -> {
                if (b.getCgpa() != a.getCgpa()) {
                    return new BigDecimal(b.getCgpa()).compareTo(new BigDecimal(a.getCgpa()));
                } else if (b.getFname() !=a.getFname()) {
                    return a.getFname().compareTo(b.getFname());
                } else  {
                    return new BigDecimal(a.getId()).compareTo(new BigDecimal(b.getId()));
                }
            });
            testCases--;
        }

        studentList.stream().forEach((x) -> System.out.println(x.getFname()));
    }
}
