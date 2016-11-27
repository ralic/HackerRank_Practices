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

//https://www.hackerrank.com/challenges/java-date-and-time

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

// Input Format :  MM /DD // YYYY, 08 05 2015

public class JavaDateTime {

    public static void main(String[] args) throws ParseException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        SimpleDateFormat oldfmt = new SimpleDateFormat("MM dd yyyy");
        Date date = oldfmt.parse(input.get(0));
        SimpleDateFormat newfmt = new SimpleDateFormat("EEEE");
        System.out.println(newfmt.format(date).toUpperCase());

    }
}

// Old school style :

//        int[] info=Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
//        System.out.println(info.length);
//        System.out.println(info[2]+" "+info[1]+" "+info[0]);
//        Date date = new Date(info[2]-1900,info[0]-1,info[1],12,0,0);
//        System.out.println(date.getDay()); // 3 -> Wednesday