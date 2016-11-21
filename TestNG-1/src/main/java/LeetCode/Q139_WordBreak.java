//import java.util.Arrays;https://leetcode.com/problems/word-break/

package LeetCode;/**
 * Created by raliclo on 21/11/2016.
 * Project Name : TestNG-1
 */

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

public class Q139_WordBreak {

    public static void main(String[] args) {

        String stringTest = "ccacccbcab";
//        String stringTest = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab";
        Set<String> stringSet = new LinkedHashSet<>();
        String[] stringlist = {"cc", "bb", "aa", "bc", "ac", "ca", "ba", "cb"};
//        String[] stringlist = {"a", "aa", "aaa", "aaaa", "aaaaa", "aaaaaa", "aaaaaaa", "aaaaaaaa", "aaaaaaaaa", "aaaaaaaaaa"};
        Arrays.stream(stringlist).forEach(e -> stringSet.add(e));
        System.out.println(stringTest + " ? " + stringSet);


        long speedX = System.currentTimeMillis();
        System.out.println(wordBreak_uber(stringTest, stringSet));
        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        System.out.println(wordBreak3(stringTest, stringSet));
        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

        // Timer for Speed Test
        speedX = System.currentTimeMillis();
        System.out.println(wordBreak(stringTest, stringSet));
        // Report for Speed Test
        System.out.println("Time spent :" + (System.currentTimeMillis() - speedX) + "msec");

    }

    // Uber fast wordbreak
    public static boolean wordBreak_uber(String s, Set<String> wordDict) {
        boolean[] possible = new boolean[s.length()];
        if (wordDict.contains(s.substring(0, 1)) == true) {
            possible[0] = true;
        }
        for (int i = 1; i < s.length(); i++) {
            for (int j = i; j >= 0; j--) {
                if (j > 0 && possible[j - 1] == true && wordDict.contains(s.substring(j, i + 1)) == true) {
                    possible[i] = true;
                    break;
                } else if (j == 0 && wordDict.contains(s.substring(0, i + 1)) == true) {
                    possible[i] = true;
                    break;
                }
            }
        }
        return possible[s.length() - 1] == true;
    }

    // SLOW code.
    public static boolean wordBreak3(String s, Set<String> wordDict) {
        int len = s.length();
        boolean[] f = new boolean[len + 1];
        f[0] = true;
        for (int i = 1; i < len + 1; i++)
            for (int j = 0; j < i; j++)
                if (f[j] && wordDict.contains(s.substring(j, i))) {
                    f[i] = true;
                    break;
                }
        return f[len];
    }

    // SLOWEST code
    public static boolean wordBreak(String s, Set<String> wordDict) {
        HashMap<String, Boolean> checked = new HashMap();
        if (checked.containsKey(s)) {
            return checked.get(s);
        }
        if (wordDict.size() > 1) {
            if (s.length() >= 0) {
                for (int i = 1; i < s.length(); i++) {
                    if (wordDict.contains(s.subSequence(0, i))
                            && wordBreak((String) s.subSequence(i, s.length()), wordDict)) {
                        checked.putIfAbsent(s, Boolean.TRUE);
                        return true;
                    }
                }
            }
        }
        if (wordDict.size() == 1) {
            return wordDict.contains(s);
        }
        return wordDict.contains(s);
    }

}
