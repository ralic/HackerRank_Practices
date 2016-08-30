package org.raliclo.HackerRank_Competitions;/**
 * Created by raliclo on 7/23/16.
 * Project Name : TestNG-1
 */
/*
Amanda has a string, , of  lowercase letters that she wants to copy into a new string, . She can perform the following operations any number of times to construct string :

Append a character to the end of string  at a cost of  dollar.
Choose any substring of  and append it to the end of  at no charge.
Given  strings (i.e., ), find and print the minimum cost of copying each  to  on a new line.

Input Format

The first line contains a single integer, , denoting the number of strings.
Each line  of the  subsequent lines contains a single string, .

Constraints

Subtasks

 for  of the maximum score.
Output Format

For each string  (where ), print the minimum cost of constructing string  on a new line.

Sample Input

2
abcd
abab
Sample Output

4
2
Explanation

Query 0: We start with  and .

Append character '' to  at a cost of  dollar, .
Append character '' to  at a cost of  dollar, .
Append character '' to  at a cost of  dollar, .
Append character '' to  at a cost of  dollar, .
Because the total cost of all operations is  dollars, we print  on a new line.

Query 1: We start with  and .

Append character '' to  at a cost of  dollar, .
Append character '' to  at a cost of  dollar, .
Append substring  to  at no cost, .
Because the total cost of all operations is  dollars, we print  on a new line.

Note

A substring of a string  is another string  that occurs "in"  (Wikipedia). For example, the substrings of the string "" are "", "" ,"", "", "", and "".
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class StringConstruction {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        for (int a0 = 0; a0 < n; a0++) {
            String s = in.next();
            HashMap<String,Integer> k = new HashMap<>();
            Arrays.stream(s.split("")).forEach((x)-> k.put(x,0));
            System.out.println(k.size());
        }
    }
}
