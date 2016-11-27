package org.raliclo.OctaveAns;
/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author raliclo
 */
public class OctaveAns {

    private static final Logger LOG = Logger.getLogger(OctaveAns.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            /**
             * @param speedX to monitor program runtime
             */
            Path currentPath = Paths.get("");
            System.out.println("Current relative path is: " + currentPath.toAbsolutePath().toString());
            long speedX = System.currentTimeMillis();
            int i = 0;
            String command = "/usr/local/bin/octave " + currentPath.toAbsolutePath().toString() + "/src/main/java/org/raliclo/OctaveAns/a.m";
            Object x1 = runexec(command);
            System.out.println(x1);
            System.out.println("Time spent :" + (System.currentTimeMillis() - speedX));
        } catch (IOException ex) {
            Logger.getLogger(OctaveAns.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param x for command line
     * @throws java.io.IOException
     */
    public static Object runexec(String x) throws IOException {
        Process p = Runtime.getRuntime().exec(x, null, null);
        Object ans = null;
        BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String line = null;
        try {
            while ((line = input.readLine()) != null) {
                if (line.startsWith("ans =")) {
                    ans = line.split("=")[1].trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            p.waitFor();

        } catch (InterruptedException ex) {
            Logger.getLogger(OctaveAns.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        return ans;
    }
}