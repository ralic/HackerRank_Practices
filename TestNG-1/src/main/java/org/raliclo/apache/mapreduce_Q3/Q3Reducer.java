package org.raliclo.apache.mapreduce_Q3;

import javafx.util.Pair;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Q3Reducer extends
        Reducer<Text, Text, Text, Text> {
    final static List<Pair<String, Integer>> bag = new ArrayList();
    //    final static PriorityQueue<Pair<String,Integer>> bag= new PriorityQueue<>(
//            sorter
//    );
    final static Comparator<Pair<String, Integer>> sorter_List = (now, next) -> next.getValue().compareTo(now.getValue());
    final static Comparator<Pair<String, Integer>> sorter_PQ = (now, next) -> next.getValue().compareTo(now.getValue());

    @Override
    protected void reduce(Text words, Iterable<Text> wordFreq,
                          Context context) throws IOException, InterruptedException {
        for (Text item : wordFreq) {
            bag.add(new Pair(words.toString(), Integer.parseInt(item.toString())));
            bag.sort(sorter_List);
            if (bag.size() > 5) {
                bag.remove(bag.size() - 1);
//                bag.poll();
            }
        }
    }

    @Override
    protected void cleanup(Context context) {
        try {
            for (Pair<String, Integer> pairInBag : bag) {
                context.write(
                        new Text(pairInBag.getKey()),
                        new Text(pairInBag.getValue().toString())
                );
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

