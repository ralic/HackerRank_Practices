package org.raliclo.apache.mapreduce_Q2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.HashMap;

public class IndexCombiner extends
        Reducer<Text, Text, Text, Text> {


    @Override
    protected void reduce(Text words, Iterable<Text> filenames,
                          Context context) throws IOException, InterruptedException {

        HashMap<String, Integer> map = new HashMap<>();
        for (Text item : filenames) {
            if (map.containsKey(item.toString())) {
                map.put(item.toString(), map.get(item.toString()) + 1);
            } else {
                map.putIfAbsent(item.toString(), 1);
            }
        }

        System.out.println(map);
        map.forEach((filename, counts) -> {
                    try {
                        context.write(words,
                                new Text("{" + filename + ":" + counts + "}"));
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
        );
    }
}
