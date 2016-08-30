package org.raliclo.HackerRank_Competitions.Contests.BookingBE;/**
 * Created by raliclo on 8/9/16.
 * Project Name : TestNG-1
 */

//https://www.hackerrank.com/contests/booking-passions-hacked-backend/challenges/a-couple-and-their-passions

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class CouplingPassions {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
        reader.close();
        int n = Integer.parseInt(input.get(0));// guest_counts;
        int y = Integer.parseInt(input.get(n + 1));// destinations;
//         Math.toRadians() Math.toDegrees()
        ArrayList<Guest> guests = new ArrayList<>();
        ArrayList<Destination> dests = new ArrayList<>();
        int i, j = 0;
        for (i = 0; i < n; i++) {
            guests.add(new Guest(input.get(1 + i)));
        }
        for (i = 0; i < y; i++) {
            dests.add(new Destination(input.get(2 + n + i)));
        }
        HashSet<String> set_passion = new HashSet<>();
        guests.stream().forEach(
                (m1) -> {
                    m1.passions.stream().forEach((x) -> set_passion.add(x));
                }
        );
        // set_passion collected
//        System.out.println(set_passion);
        // Create destpair
        ArrayList<DestinationPair> destpairlist = new ArrayList<>();
        for (i = 0; i < dests.size(); i++) {
            for (j = i + 1; j < dests.size(); j++) {
                destpairlist.add(new DestinationPair(dests.get(i), dests.get(j), set_passion));
            }
        }
        destpairlist.sort(
                (m7,m8) -> {
                    int cmp1=Integer.compare(m8.uniqs,m7.uniqs);
                    int cmp2=Double.compare(m7.distpair,m8.distpair);
                    if (cmp1>0 || cmp1<0) {
                        return cmp1;
                    } else {
                        return cmp2;
                    }
                }
        );

//        destpairlist.forEach((m3) -> {
//            System.out.println(m3.start + " " + m3.end + " " + m3.distpair + " " + m3.uniqs);
//        });

        System.out.println(destpairlist.get(0).start+" "+destpairlist.get(0).end);

//            int[] counter = new int[1];
//        HashMap<String, Integer> set_destination = new HashMap<>();
//
//        dests.stream().forEach(
//                (m2) -> {
//                    counter[0] = 0;
//                    m2.acts.forEach(
//                            (m3) -> {
//                                if (set_passion.contains(m3)) {
//                                    counter[0]++;
//                                }
//                            }
//                    );
//                    set_destination.put(m2.name, counter[0]);
//                }
//        );
//        System.out.println(set_destination);
        input = null;
    }

    public static class Guest {
        ArrayList<String> passions = new ArrayList<>();

        Guest(String x) {
            String[] tmp = x.split(" ");
            for (int i = 0; i < Integer.parseInt(tmp[0]); i++) {
                passions.add(tmp[1 + i]);
            }
        }
    }

    public static class Destination {
        String name;
        double lat; // in Radian
        double lng; // in Radian
        ArrayList<String> acts = new ArrayList<>();

        Destination(String x) {
            String[] tmp = x.split(" ");
            name = tmp[0];
            lat = Math.toRadians(Double.valueOf(tmp[1]));
            lng = Math.toRadians(Double.valueOf(tmp[2]));
            for (int i = 0; i < Integer.parseInt(tmp[3]); i++) {
                acts.add(tmp[4 + i]);
            }
        }

    }

    public static class DestinationPair {
        String start;
        String end;
        double distpair;
        int uniqs;

        DestinationPair(Destination p1, Destination p2, HashSet<String> set_passion) {
            int k = p1.name.compareTo(p2.name);
            if (k > 0) {
                start = p2.name;
                end = p1.name;
            } else if (k < 0) {
                start = p1.name;
                end = p2.name;
            }
            distpair = distance(p1, p2);
            uniqs = 0;
            HashSet<String> ppairs = new HashSet<>();
            p1.acts.forEach((m3) -> {
                ppairs.add(m3);
            });
            p2.acts.forEach((m4) -> {
                ppairs.add(m4);
            });
//            System.out.println("ppairs"+ppairs);
            ppairs.forEach((m5) -> {
                        if (set_passion.contains(m5)) {
                            uniqs++;
                        }
                    }
            );
        }
    }

    public static double distance(Destination p1, Destination p2) {
        int EARTH_RADIUS = 6371;//in km
        return Math.acos(Math.sin(p1.lat) * Math.sin(p2.lat)
                + Math.cos(p1.lat) * Math.cos(p2.lat) * Math.cos(p2.lng - p1.lng)) * EARTH_RADIUS;
    }
}

