package org.raliclo.Files_Path_Operation;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import org.raliclo.WordCountWeb.src.RunCommand;

import java.util.ArrayList;

public class MemoryInfo {

    public static void topten() {
        System.out.println();
        ArrayList<String> ans = (ArrayList<String>) RunCommand.runexec("top -l 1 -s 0");
        int i = 0;
        while (i++ < 10) {
            System.out.println(ans.get(i));
        }
    }

    public static void main(String[] args) {
        topten();
    }
}
