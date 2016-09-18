package org.raliclo.apache.mapreduce_Q2;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class IndexReducer extends
        Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text words, Iterable<Text> filenameCounts,
                          Context context) throws IOException, InterruptedException {

        String finalText = "";
        for (Text item : filenameCounts) {
            finalText = finalText.concat(item.toString()).concat(",");
        }

        context.write(
                words,
                new Text(finalText));
    }
}

