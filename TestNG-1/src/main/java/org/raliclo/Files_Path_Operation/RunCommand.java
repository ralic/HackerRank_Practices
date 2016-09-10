package org.raliclo.Files_Path_Operation;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class RunCommand {
    String command;

    RunCommand(String x) {
        this.command = x;
    }

    public static Object runexec(String x) {
        long speedX = System.nanoTime();

        try {
            final Process p = Runtime.getRuntime().exec(x);
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
            reader.close();
            System.out.println(System.nanoTime() - speedX + " (n)secs");
            return input;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        ArrayList<String> output = (ArrayList<String>) RunCommand.runexec("ls -l -a");
        System.out.println(output);
        output.stream().forEach(System.out::println);
    }
}
