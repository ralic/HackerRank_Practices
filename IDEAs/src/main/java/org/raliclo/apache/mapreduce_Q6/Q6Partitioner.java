package org.raliclo.apache.mapreduce_Q6;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

public class Q6Partitioner extends Partitioner<Text, Text> {

    @Override
    public int getPartition(Text key, Text value, int numReduceTasks) {

        if (numReduceTasks == 0) {
            return 0;
        }
        String className = key.toString().split(",")[0];
        if (className.equals("CS")) {
            System.out.println(key);
            return 1;
        } else {
            return 0;
        }
    }
}
