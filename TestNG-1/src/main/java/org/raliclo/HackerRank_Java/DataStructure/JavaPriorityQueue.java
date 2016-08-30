package org.raliclo.HackerRank_Java.DataStructure;

//https://www.hackerrank.com/challenges/java-priority-queue

/**
 * Created by raliclo on 8/20/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

class StudentQ {
    private int token;
    private String fname;
    private double cgpa;

    public StudentQ(String fname, double cgpa, int id) {
        super();
        this.token = id;
        this.fname = fname;
        this.cgpa = cgpa;
    }

    public int getToken() {
        return token;
    }

    public String getFname() {
        return fname;
    }

    public double getCgpa() {
        return cgpa;
    }
}


public class JavaPriorityQueue {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        int n = Integer.parseInt(input.get(0));

//         Define comparator
        Comparator<StudentQ> sorter = (StudentQ o1, StudentQ o2) -> {
            if (o2.getCgpa() == o1.getCgpa()){
                if(o2.getFname().equals(o1.getFname())){
                    return (o2.getToken() > o1.getToken())? -1: 1;
                } else {
                    return -o2.getFname().compareTo(o1.getFname());
                }
            } else {
                return (o2.getCgpa() > o1.getCgpa())? 1: -1;
            }
        };

//         arrange queue
        PriorityQueue<StudentQ> pq = new PriorityQueue<>(sorter);

        for (int i = 1; i < n + 1; i++) {
            options(input.get(i), pq);
        }

        if (pq.size() == 0)
            System.out.println("EMPTY");
        else {
            while (pq.size() > 0) {
                System.out.println(pq.poll().getFname()); // Print polled student's name
            }
        }
    }

    public static void options(String opts, PriorityQueue<StudentQ> pq) {
        String[] info = opts.split(" ");
//        System.out.println(info.length);
        StudentQ st;
        switch (info[0]) {
            case "ENTER":
                st = new StudentQ(info[1], Double.parseDouble(info[2]), Integer.parseInt(info[3]));
                pq.add(st);
                break;
            case "SERVED":
                if (pq.size() > 0) {
                    pq.poll();
                }
                break;
        }
    }
}
