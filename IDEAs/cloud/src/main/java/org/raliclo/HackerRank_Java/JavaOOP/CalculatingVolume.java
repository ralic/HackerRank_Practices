/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.raliclo.HackerRank_Java.JavaOOP;/**
 * Created by raliclo on 8/21/16.
 * Project Name : TestNG-1
 */

import java.io.IOException;
import java.security.Permission;
import java.util.Scanner;

//Fix Above
//Write your code here
class Calculate {
    static Result result = new Result();
    static Output output = new Output();
    static Scanner in = new Scanner(System.in);

    public static Result do_calc() {
        return result;
    }

    public static int get_int_val() throws IOException {
        int ans;
        try {
            ans = in.nextInt();
        } catch (Exception ex) {
            throw new IOException();
        }
        return ans;
    }

    public static double get_double_val() throws IOException {
        double ans;
        try {
            ans = in.nextDouble();
        } catch (Exception ex) {
            throw new IOException();
        }
        return ans;
    }


    public static class Result {

        public static double get_volume(int l, int b, int h) {
            return l * b * h;
        }

        public static double get_volume(double r) {
            return Math.PI * r * r * r * 4 / 3 / 2;
        }

        public static double get_volume(double r, double h) {
            return Math.PI * r * r * h;
        }

        public double get_volume(int a) {
            return a * a * a;
        }
    }

    public static class Output {
        public void display(double volume) {
            if (volume <= 0) {
                throw new NumberFormatException("All the values must be positive");
            } else {
                System.out.printf("%.3f", volume);
                System.out.println();
            }
        }
    }

}

// Fixed below
class CalculatingVolume {

    public static void main(String[] args) {
        DoNotTerminate.forbidExit();
        try {
            Calculate cal = new Calculate();
            int T = cal.get_int_val();
            while (T-- > 0) {
                double volume = 0.0;
                int ch = cal.get_int_val();
                if (ch == 1) {
                    int a = cal.get_int_val();
                    volume = Calculate.do_calc().get_volume(a);
                } else if (ch == 2) {
                    int l = cal.get_int_val();
                    int b = cal.get_int_val();
                    int h = cal.get_int_val();
                    volume = Calculate.do_calc().get_volume(l, b, h);
                } else if (ch == 3) {
                    double r = cal.get_double_val();
                    volume = Calculate.do_calc().get_volume(r);
                } else if (ch == 4) {
                    double r = cal.get_double_val();
                    double h = cal.get_double_val();
                    volume = Calculate.do_calc().get_volume(r, h);
                }
                cal.output.display(volume);
            }

        } catch (NumberFormatException e) {
            System.out.print(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DoNotTerminate.ExitTrappedException e) {
            System.out.println("Unsuccessful Termination!!");
        }


    } //end of main
} //end of Solution

/**
 * This class prevents the user form using System.exit(0)
 * from terminating the program abnormally.
 */
class DoNotTerminate {

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

    public static class ExitTrappedException extends SecurityException {
    }
} //end of Do_Not_Terminate
