package org.raliclo.apache.mapreduce_Q5;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

public class Q5Partitioner extends Partitioner<Text, Text> {

    @Override
    public int getPartition(Text key, Text value, int numReduceTasks) {

        if (numReduceTasks == 0) {
            return 0;
        }
        String deptName = value.toString().split("_")[3];
        switch (deptName) {
            case "BUS":
                return 1;
            case "CS":
                return 2;
            case "MATH":
                return 3;
            case "ENG":
                return 4;
            default:
                return 0;
        }
    }
}
