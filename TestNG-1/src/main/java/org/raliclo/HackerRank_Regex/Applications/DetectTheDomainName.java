
//https://www.hackerrank.com/challenges/detect-the-domain-name
package org.raliclo.HackerRank_Regex.Applications;

/**
 * Created by raliclo on 8/31/16.
 * Project Name:TestNG-1
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DetectTheDomainName {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

//        Comparator<String> sorter = (a, b) -> a.compareTo(b);

        ArrayList<String> bag = new ArrayList<>();
        for (int i = 0; i < input.size(); i++) {
            PrintDomain tester = new PrintDomain();
            tester.checker(input.get(i), "(http(s)?:\\/\\/)([a-zA-Z0-9-]*)(\\.)([a-zA-Z0-9\\-\\.]*)", bag);
        }
        bag.sort((a, b) -> b.compareTo(a));
        Object[] ans = bag.stream().distinct().toArray();
        int n = ans.length;
        while (n-- > 1) {
            System.out.print(ans[n] + ";");
        }
        System.out.println(ans[n]);
    }

    static class PrintDomain {

        public void checker(String data, String pattern, ArrayList<String> bag) {
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(data);
            while (m.find())
                if (!bag.contains(m.group(3))) {
                    Pattern bug = Pattern.compile("^[w0-9]{3}$");
                    Matcher exclude = bug.matcher(m.group(3));
                    if (!exclude.find()) {
                        bag.add(m.group(3).concat(m.group(4)).concat(m.group(5)));
                    } else {
                        bag.add(m.group(5));
                    }
                }
        }

    }
}
