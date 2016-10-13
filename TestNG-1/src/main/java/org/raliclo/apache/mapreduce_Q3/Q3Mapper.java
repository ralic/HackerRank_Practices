package org.raliclo.apache.mapreduce_Q3;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Q3Mapper extends
        Mapper<Object, Text, Text, Text> {

    @Override
    protected void map(Object key, Text value, Context context) throws IOException, InterruptedException {


        String[] words = value.toString().split(" ");
        context.write(new Text(words[0]), new Text(words[1]));

    }
}

