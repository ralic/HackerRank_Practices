package org.raliclo.apache.mapreduce_Q4;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Q4Reducer extends
        Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text year, Iterable<Text> datas,
                          Context context) throws IOException, InterruptedException {

        for (Text item : datas) {
            context.write(year, item);
        }
    }
}

