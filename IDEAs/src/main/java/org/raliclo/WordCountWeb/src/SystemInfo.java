package org.raliclo.WordCountWeb.src;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SystemInfo {
    public static Object tops() {
        final ArrayList<String>[] ans = new ArrayList[2];
        Runnable worker = () -> {
            System.out.println();
            ans[0] = (ArrayList<String>) RunCommand.runexec("top -l 1 -s 0");
            ans[1] = new ArrayList<>();
            int i = 0;
            while (i++ < 10) {
                ans[1].add(ans[0].get(i));
                /* For Printing out all top 9 factors
                * System.out.println(ans[1].get(i-1));
                * */
            }
        };
        new Thread(worker).run();
        String x = (String) checker("CPU usage: \\S*% user, \\S*% sys, ([0-9.]{2,})% idle", ans[1].get(2));
        return Double.parseDouble(x);
    }

    public static Object checker(String Regex_Pattern, String Test_String) {
        Pattern p = Pattern.compile(Regex_Pattern);
        Matcher m = p.matcher(Test_String);
        m.find();
        return m.group(1);
    }

}
