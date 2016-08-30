package org.raliclo.HackerRank_Competitions;

/**
 * Created by raliclo on 7/23/16.
 * Project Name : TestNG-1
 */

/*
Alice wrote a sequence of words in CamelCase as a string of letters, , having the following properties:

It is a concatenation of one or more words consisting of English letters.
All letters in the first word are lowercase.
For each of the subsequent words, the first letter is uppercase and rest of the letters are lowercase.
Given , print the number of words in  on a new line.

Input Format

A single line containing string .

Constraints

Output Format

Print the number of words in string .

Sample Input

saveChangesInTheEditor
Sample Output

5
Explanation

String  contains five words:

save
Changes
In
The
Editor
Thus, we print  on a new line.
 */

public class CamelCase {

    public static void main(String[] args) {
//        Scanner in = new Scanner(System.in);
//        String s = in.next();
        String s = "saveChangesInTheEditor";
//        int[] list=IntStream.range(65,91).toArray();
//        System.out.println((int)'z');
        int ans = (s.charAt(0) > 96 && s.charAt(0)<123 )? 1:0;
        for (int i = 0; i < s.length(); i++) {
            if ((int) s.charAt(i) > 64 && (int) s.charAt(i) < 91) {
                ans++;
            }
        }
        System.out.println(ans);
    }
}
