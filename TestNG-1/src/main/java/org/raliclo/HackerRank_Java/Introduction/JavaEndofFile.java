package org.raliclo.HackerRank_Java.Introduction;

/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class JavaEndofFile {
    private static int lines=1;
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> x = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        x.forEach((str) -> {System.out.println(lines+" "+str);lines++;});
    }
}
