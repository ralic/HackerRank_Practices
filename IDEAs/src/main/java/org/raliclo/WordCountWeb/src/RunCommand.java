package org.raliclo.WordCountWeb.src;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RunCommand {
    public static Process process;
    static String command;

    public RunCommand() {
    }

    public RunCommand(String x) {
        command = x;
    }

    public static Object runexec(String x) {
        try {
            process = Runtime.getRuntime().exec(x);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
            return input;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
