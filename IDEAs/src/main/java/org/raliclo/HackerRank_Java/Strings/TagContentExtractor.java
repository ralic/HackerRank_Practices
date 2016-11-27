package org.raliclo.HackerRank_Java.Strings;/**
 * Created by raliclo on 7/19/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/tag-content-extractor

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TagContentExtractor {
    public static void main(String[] args) {
        // link-1 : http://www.rubular.com/r/kn4ZMtBnny
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());
        while (testCases > 0) {
            String line = in.nextLine();
            //<(.+?)>([^<>]+)</\\1>
//            Pattern p2 = Pattern.compile("(<)([A-Za-z0-9 ]+)(>)+([a-zA-Z0-9 ]*)(<\\/)([\\w ]+)(>)");
//            Pattern p = Pattern.compile("<([A-Za-z0-9 ]+)>+([^<>]+)<\\/([A-Za-z0-9 ]+)>");
            Pattern p = Pattern.compile("<([^<>]+)>+([^<>]+)<\\/([^<>]+)>");
            Matcher m = p.matcher(line);
            boolean flag = m.find();
//            System.out.println(m.toMatchResult());

            if (!flag) {
                System.out.println("None-- not in flag");
            }
            while (flag) {
//                System.out.println(m.group(1) + " " + m.group(2) + " " + m.group(3));
                if (m.group(1).equals(m.group(3)) && !m.group(1).equals("") && !m.group(3).equals("")) {
                    if (!m.group(2).equals("")) {
                        System.out.println(m.group(2));
                    } else {
                        System.out.println("None-- result is null");
                    }
                } else {
                    System.out.println("None-- g1 not same as g3");
                }
                flag = m.find();
            }
            testCases--;
        }
    }
}


class Solution {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int testCases = Integer.parseInt(in.nextLine());
        while (testCases > 0) {
            String line = in.nextLine();
            int cur = 0;
            boolean none = true;
            for (; ; ) {
                int start = line.indexOf("<", cur);
                if (start < 0) break;
                int end = line.indexOf(">", start);
                if (end < 0) break;
                String tag = line.substring(start + 1, end);
                if (tag.length() == 0 || tag.charAt(0) == '/') {
                    cur = end + 1;
                    continue;
                }
                int bk = line.indexOf("</" + tag + ">");
                if (bk >= 0) {
                    String candidate = line.substring(end + 1, bk);
                    if (candidate.length() > 0 && candidate.indexOf("<") < 0) {
                        none = false;
                        System.out.println(candidate);
                    }
                }
                cur = end + 1;
            }
            if (none) System.out.println("None");
            testCases--;
        }
    }
}