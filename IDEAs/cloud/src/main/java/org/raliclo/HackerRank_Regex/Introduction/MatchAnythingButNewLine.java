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

package org.raliclo.HackerRank_Regex.Introduction;/**
 * Created by raliclo on 8/31/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/challenges/matching-anything-but-new-line

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchAnythingButNewLine {

    public static void main(String[] args) {
        Regex_Test tester = new Regex_Test();
        tester.check("^(\\S{3}\\.){3}\\S{3}$");
    }

    static class Regex_Test {

        public void check(String pattern) {

            Scanner scanner = new Scanner(System.in);
            String testString = scanner.nextLine();
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(testString);
            boolean match = m.matches();
            System.out.format("%s", match);
        }

    }

}