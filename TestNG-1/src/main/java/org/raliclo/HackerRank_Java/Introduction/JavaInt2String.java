package org.raliclo.HackerRank_Java.Introduction;/**
 * Created by raliclo on 7/18/16.
 * Project Name : TestNG-1
 */

import java.security.Permission;
import java.util.Scanner;

//https://www.hackerrank.com/challenges/java-int-to-string?h_r=next-challenge&h_v=zen
public class JavaInt2String {

    public static void main(String[] args) {

        Do_Not_Terminate.forbidExit();

        try {
            Scanner in = new Scanner(System.in);
            int n = in.nextInt();
            //String s=???; Complete this line below

            // Default above
            //Write your code here
            String s = String.valueOf(n);

            // Default below
            if (n == Integer.parseInt(s)) {
                System.out.println("Good job");
            } else {
                System.out.println("Wrong answer.");
            }
        } catch (Do_Not_Terminate.ExitTrappedException e) {
            System.out.println("Unsuccessful Termination!!");
        }
    }
}

//The following class will prevent you from terminating the code using exit(0)!
class Do_Not_Terminate {

    public static class ExitTrappedException extends SecurityException {

        private static final long serialVersionUID = 1L;
    }

    public static void forbidExit() {
        final SecurityManager securityManager = new SecurityManager() {
            @Override
            public void checkPermission(Permission permission) {
                if (permission.getName().contains("exitVM")) {
                    throw new ExitTrappedException();
                }
            }
        };
        System.setSecurityManager(securityManager);
    }
}


