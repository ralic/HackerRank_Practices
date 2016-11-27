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

package org.raliclo.HackerRank_Java.ExceptionHandling;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/challenges/java-exception-handling-try-catch

import java.util.Scanner;

public class JavaExceptionHandling {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        try {
            int in1, in2;
            in1 = in.nextInt();
            in2 = in.nextInt();
            if (in2 == 0) {
                throw new ArithmeticException();
            }
            System.out.println(in1 / in2);
        } catch (Exception ex) {
            if (ex.getClass().getName() == "java.lang.ArithmeticException") {
                System.out.println(ex.getClass().getName() + ": / by zero");
            } else {
                System.out.println(ex.getClass().getName());
            }
        }
    }

}
