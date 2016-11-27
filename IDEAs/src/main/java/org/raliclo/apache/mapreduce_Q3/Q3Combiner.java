package org.raliclo.apache.mapreduce_Q3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Q3Combiner extends
        Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text words, Iterable<Text> wordFreq,
                          Context context) throws IOException, InterruptedException {

        // remove duplication of key.
        Integer count = 0;
        for (Text item : wordFreq) {
            count = count + Integer.parseInt(item.toString());
        }

        context.write(words, new Text(count.toString()));
    }
}
