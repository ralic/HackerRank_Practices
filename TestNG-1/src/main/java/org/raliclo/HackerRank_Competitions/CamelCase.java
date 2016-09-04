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
