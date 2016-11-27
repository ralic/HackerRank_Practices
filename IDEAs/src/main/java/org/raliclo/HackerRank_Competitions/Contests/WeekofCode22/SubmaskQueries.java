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

package org.raliclo.HackerRank_Competitions.Contests.WeekofCode22;/**
 * Created by raliclo on 8/12/16.
 * Project Name : TestNG-1
 */
//https://www.hackerrank.com/contests/w22/challenges/submask-queries-

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class SubmaskQueries {

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        HashMap<String, Integer> storage = new HashMap<>();

        int[] n_m = Arrays.stream(input.get(0).split(" ")).mapToInt(Integer::parseInt).toArray();
        for (int i = 1; i < n_m[1] + 1; i++) {
            System.out.println(input.get(i));
            parser(input.get(i), storage);
        }


    }


    public static void parser(String str, HashMap<String, Integer> storage) {
        String[] info = str.split(" ");
        int value;
        String[] strlist;
        Set<Set<Object>> resp;
        BitSet bitset;
        switch (info[0]) {
            case "1": // Create
                value = Integer.parseInt(info[1]);
                strlist = info[2].split("");
                bitset = new BitSet(strlist.length);
                for (int i = 0; i < strlist.length; i++) {
                    if (strlist[i].equals("1")) {
                        bitset.set(i + 1);
                    }
                }

                System.out.println(bitset);
//                resp=getSubsets();

                break;
            case "2": // Update with XOR
                value = Integer.parseInt(info[1]);
                strlist = info[2].split("");
                bitset = new BitSet(strlist.length);
                for (int i = 0; i < strlist.length; i++) {
                    if (strlist[i].equals("1")) {
                        bitset.set(i + 1);
                    }
                }
                System.out.println(bitset);


                break;
            case "3":
//                strlist = info[1].split("");

                break;
        }

    }

    private static Set<Set<Object>> getSubsets(List<Object> list) {
        Set<Set<Object>> result = new HashSet<Set<Object>>();
        int numOfSubsets = 1 << list.size(); //OR Math.pow(2, list.size())
        // For i from 0 to 7 in case of [a, b, c],
        // we will pick 0(0,0,0) and check each bits to see any bit is set,
        // If set then element at corresponding position in a given Set need to be included in a subset.
        for (int i = 0; i < numOfSubsets; i++) {
            Set<Object> subset = new HashSet<Object>();
            int mask = 1; // we will use this mask to check any bit is set in binary representation of value i.
            for (int k = 0; k < list.size(); k++) {
                if ((mask & i) != 0) { // If result is !=0 (or >0) then bit is set.
                    subset.add(list.get(k)); // include the corresponding element from a given set in a subset.
                }
                // check next bit in i.
                mask = mask << 1;
            }
            if (!subset.isEmpty())
                // add all subsets in final result.
                result.add(subset);
        }
        return result;
    }

}


// Skip first 20 subset.
/*
Set<Integer> subSet = set.stream().skip(20).collect(toCollection(LinkedHashSet::new));
    // You could also collect to something else with another collector like this:
    // .collect(toList());
 */

/*
youSet.stream()
   .skip(start) // the offset
   .limit(count) // how many items you want
   .collect(Collectors.toSet());
 */

/*
public static <T> List<T> listOf(final Collection<T> set, final int limit) {
    final List<T> list = new ArrayList<>(limit);

    final Iterator<T> i = set.iterator();
    for (int j = 0; j < limit && i.hasNext(); j++) {
        list.add(i.next());
    }

    return list;
}
 */

// LinekdHashSet example
/*
      LinkedHashSet hs = new LinkedHashSet(); // Ordered, Sorted HashSet.
        // add elements to the hash set
        hs.add("B");
        hs.add("A");
        hs.add("D");
        hs.add("E");
        hs.add("C");
        hs.add("F");
        System.out.println(hs);
        LinkedList<String> hs2 = new LinkedList<>(hs); // Transfer set into linkedlist
        System.out.println(hs2);
        List<String> hs3= hs2.subList(1,hs2.size()); // Trasfrom linkedlist into list
        System.out.println(hs3);

 */


/*
Set<Integer> set = new LinkedHashSet<>();
for (int i = 0; i < 50; i++) {
   set.add(i);
}

List<Integer> list = new ArrayList<>(set);
Set<Integer> subSet = new LinkedHashSet<>(list.subList(0, 20));
 */
