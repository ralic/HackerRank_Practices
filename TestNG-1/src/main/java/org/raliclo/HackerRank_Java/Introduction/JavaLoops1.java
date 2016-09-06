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

package org.raliclo.HackerRank_Java.Introduction;/**
 * Created by raliclo on 8/2/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/java-loops-i

import java.util.Scanner;
import java.util.stream.IntStream;

public class JavaLoops1 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int a = in.nextInt();
        IntStream.range(1, 11).forEach((m1) -> System.out.println(a + " x " + m1 + " = " + m1 * a));
    }
}
