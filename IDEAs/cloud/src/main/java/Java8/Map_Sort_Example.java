package Java8;

import avro.shaded.com.google.common.collect.Lists;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by raliclo on 23/02/2017.
 */
public class Map_Sort_Example {

    public static void main(String[] args) {
        Comparator<Integer> desc = (h1, h2) -> h2.compareTo(h1);
        Comparator<Integer> asec = (h1, h2) -> h1.compareTo(h2);

        List<Integer> datas = Lists.newArrayList(
                5, 7, 9, 10, 23, 12, 94
        );
        System.out.println(datas);
        datas.sort(asec);
        System.out.println(datas);
        datas.sort(desc);
        System.out.println(datas);


        // We'll be using this simple map
        // Unfortunately, still no map literals in Java 8..
        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);
        map.put("C", 3);
        System.out.println("Map Original" + map);

        // Compute a new value for the existing key
        System.out.println(map.compute("A",
                (k, v) -> v == null ? 42 : v + 41));
        System.out.println("Map after compute A:" + map);

        // This will add a new (key, value) pair
        System.out.println(map.compute("X",
                (k, v) -> v == null ? 42 : v + 41));
        System.out.println("Map after compute X:" + map);

        //map.merge(key, msg, String::concat)
        /* Code Traslation
        String value = map.get(key);
        if (value == null)
            map.put(key, msg);
        else
            map.put(key, value.concat(msg));
        */
    }


}


