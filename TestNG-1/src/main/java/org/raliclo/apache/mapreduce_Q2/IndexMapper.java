package org.raliclo.apache.mapreduce_Q2;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;


public class IndexMapper extends
        Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() >= 0) {
            String filename = ((FileSplit) context.getInputSplit()).getPath().getName();
            String[] words = value.toString().split(" ");
            int items = words.length;

            for (int n = 0; n < items; n++) {
                context.write(
                        new Text(words[n]),
                        new Text(filename));
            }
        }


    }
}

