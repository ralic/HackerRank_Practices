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
                } else if (b.getFname() != a.getFname()) {
                    return a.getFname().compareTo(b.getFname());
                } else {
                    return new BigDecimal(a.getId()).compareTo(new BigDecimal(b.getId()));
                }
            });
            testCases--;
        }

        studentList.stream().forEach((x) -> System.out.println(x.getFname()));
    }
}
