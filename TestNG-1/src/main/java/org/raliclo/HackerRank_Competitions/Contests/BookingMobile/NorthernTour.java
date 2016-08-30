package org.raliclo.HackerRank_Competitions.Contests.BookingMobile;/**
 * Created by raliclo on 8/11/16.
 * Project Name : TestNG-1
 */
// TODO : Code Improvment
//https://www.hackerrank.com/contests/booking-com-passions-hacked-mobile/challenges/northern-tour-1

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Collectors;

public class NorthernTour {


    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));

        int n = Integer.parseInt(input.get(0));
        int e = Integer.parseInt(input.get(n + 1));
//        System.out.println(n);
//        System.out.println(e);
        ArrayList<CityInfo> cities = CityInfo.cities;
        cities.add(new CityInfo("Bevagna", "0"));
        for (int i = 1; i < n + 1; i++) {
            String[] tmp = input.get(i).split(",");
            cities.add(new CityInfo(tmp[0], tmp[1]));
        }
        ArrayList<CityPath> cityPaths = CityPath.cityPaths;
        for (int i = n + 2; i < n + 2 + e; i++) {
            String[] tmp = input.get(i).split(",");
            cityPaths.add(new CityPath(tmp[0], tmp[1], tmp[2], cities));
        }
        ArrayList<String> route = new ArrayList<>();
        ArrayList<ArrayList<String>> routelist = new ArrayList<>();
//        HashSet<String> visited = new HashSet<>();
        findAllRoute(cities, cityPaths, route, "Bevagna", routelist);
        routelist.sort((a, b) -> Integer.compare(b.size(), a.size()));
//        routelist.forEach(System.out::println);


//        for (int i = 1; i < routelist.get(0).size(); i++) {
//            System.out.println(routelist.get(0).get(i));
//        }

//        Print info.
//        cityPaths.forEach((k) -> {
//                    System.out.println(k.flyBetween + " " + k.flyTime);
//                }
//        );

//        for (CityInfo city : cities) {
//            System.out.println(city.city + " " + city.stayTime);
//            System.out.println(city.connected);
//        }
        for (int i = 0; i < routelist.size(); i++) {
            if (checkRoute(cities, cityPaths, routelist.get(i))) {
                for (int j = 1; j < routelist.get(i).size(); j++) {
                    System.out.println(routelist.get(i).get(j));
                }
                return;
            }
        }
        System.out.println("NONE");
    }

    public static boolean checkRoute(ArrayList<CityInfo> cities, ArrayList<CityPath> cityPaths, ArrayList<String> route) {
        int workhr = 24 - 8;
        int dayElapse = 7 - 1;
        int travelledT = 0;
        int travelledD = 0;
        int boardTime = 8;
        for (int i = 0; i < route.size() - 1; i++) {
            for (int j = 0; j < cityPaths.size(); j++) {
                if (cityPaths.get(j).flyBetween.contains(route.get(i)) && cityPaths.get(j).flyBetween.contains(route.get(i + 1))) {
                    if (cityPaths.get(j).flyTime > workhr) {
                        return false;
                    } else {
                        if (cityPaths.get(j).flyTime + boardTime >= 24) {
                            travelledT += (24 - boardTime);
                            travelledD++;
                            boardTime = 8;
                        }
                        travelledT += cityPaths.get(j).flyTime;
                        for (int k = 0; k < cities.size(); k++) {
                            if (cities.get(k).city.equals(route.get(i))) {
                                travelledT += cities.get(k).stayTime;
                                int cityTime = (cities.get(k).stayTime + cityPaths.get(i).flyTime);
                                travelledD += Math.floor((cityTime + boardTime)/24);
                                boardTime = (cityTime + boardTime) % 24;
                            }
                        }

                        if (travelledT >= dayElapse * workhr || travelledD > dayElapse) {
                            return false;
                        }
                    }
                }
            }
        }
//        System.out.println(travelledT);
//        System.out.println(travelledD);
        return true;
    }

    public static void findAllRoute(ArrayList<CityInfo> cities, ArrayList<CityPath> cityPaths, ArrayList<String> route, String start, ArrayList<ArrayList<String>> routelist) {
        cities.forEach(
                (m1) -> {
                    if (m1.city.equals(start)) {
                        route.add(start);
                        m1.connected.forEach(
                                (m2) -> {
                                    if (!route.contains(m2)) {
                                        ArrayList<String> newroute = (ArrayList<String>) route.clone();
                                        routelist.add(newroute);
                                        findAllRoute(cities, cityPaths, newroute, m2, routelist);
                                    }
                                }
                        );

                    }
                }

        );
    }

    public static class CityInfo {
        final static ArrayList<CityInfo> cities = new ArrayList<>();
        String city;
        int stayTime;
        HashSet<String> connected = new HashSet<>();

        CityInfo(String city, String stayTime) {
            this.city = city;
            this.stayTime = Integer.parseInt(stayTime);
        }
    }

    public static class CityPath {
        final static ArrayList<CityPath> cityPaths = new ArrayList<>();
        HashSet<String> flyBetween = new HashSet<>();
        int flyTime;

        CityPath(String u, String w, String h, ArrayList<CityInfo> cities) {
            flyBetween.add(u);
            flyBetween.add(w);
            this.flyTime = Integer.parseInt(h);
            cities.forEach((m1) -> {
                if (m1.city.equals(u)) {
                    m1.connected.add(w);
                }
            });
            cities.forEach((m2) -> {
                if (m2.city.equals(w)) {
                    m2.connected.add(u);
                }
            });
        }
    }
}

/* Input
18
Amsterdam,9
California,6
Prag,2
Heg,4
Khulna,8
Mumbai,5
Delhi,5
Kabul,1
Colombo,9
Bali,7
Sydney,4
Rome,7
Kathmundu,2
Pokhra,4
Mayami,1
Vicecity,2
Sanandreas,6
Sandm,5
12
Bevagna,Amsterdam,7
Amsterdam,California,7
California,Prag,5
Prag,Heg,3
Heg,Khulna,5
Khulna,Mumbai,5
Mumbai,Delhi,6
Delhi,Kabul,6
Kabul,Colombo,1
Colombo,Bali,2
Bali,Sydney,7
Sydney,Rome,8
 */

/* Out
Amsterdam
California
Prag
Heg
Khulna
Mumbai
Delhi
Kabul
 */

/* Input
20
Amsterdam,1
California,2
Prag,2
Heg,2
Khulna,2
Mumbai,1
Delhi,1
Kabul,1
Colombo,1
Bali,1
Sydney,2
Rome,1
Kathmundu,2
Pokhra,20
Mayami,10
Vicecity,2
Sanandreas,2
Sandm,1
Laddu,1
Golla,1
17
Bevagna,Amsterdam,1
Amsterdam,California,1
California,Prag,1
Prag,Heg,1
Heg,Khulna,1
Khulna,Mumbai,1
Mumbai,Delhi,2
Delhi,Kabul,2
Kabul,Colombo,1
Colombo,Bali,2
Bali,Sydney,1
Sydney,Rome,2
Rome,Kathmundu,12
Kathmundu,Pokhra,8
Pokhra,Mayami,5
Mayami,Vicecity,10
Vicecity,Sanandreas,2
 */

/*
Amsterdam
California
Prag
Heg
Khulna
Mumbai
Delhi
Kabul
Colombo
Bali
Sydney
Rome
Kathmundu
Pokhra
Mayami
 */

/* Input
18
Amsterdam,30
California,30
Prag,30
Heg,30
Khulna,30
Mumbai,30
Delhi,30
Kabul,30
Colombo,30
Bali,30
Sydney,30
Rome,30
Kathmundu,30
Pokhra,30
Mayami,30
Vicecity,30
Sanandreas,30
Sandm,30
10
Khulna,Prag,30
Bevagna,Prag,30
Bevagna,Mumbai,30
Bevagna,Sydney,30
Bali,Khulna,30
Bevagna,Heg,30
Prag,Colombo,30
Bevagna,California,30
Pokhra,Rome,30
Bevagna,Kabul,30
 */

/* Out
NONE
 */

/*
6
Amsterdam,3
California,23
Prag,12
Heg,13
Khulna,20
Mumbai,16
4
California,Prag,16
California,Bevagna,15
Heg,Khulna,18
Heg,Bevagna,7
 */

/*
California
Prag
 */

/*
12
Amsterdam,7
California,5
Prag,20
Heg,14
Khulna,24
Mumbai,3
Delhi,12
Kabul,17
Colombo,23
Bali,20
Sydney,15
Rome,8
16
Amsterdam,Heg,5
Sydney,Khulna,12
Mumbai,Prag,24
California,Heg,6
Mumbai,Bali,10
Amsterdam,Prag,9
Kabul,Delhi,1
Delhi,Khulna,7
California,Colombo,16
Bevagna,Kabul,9
Bevagna,California,1
Mumbai,Colombo,16
Amsterdam,Khulna,21
Sydney,Kabul,22
Mumbai,Amsterdam,23
Bali,Delhi,18
 */

/*
California
Heg
Amsterdam
Prag
 */
