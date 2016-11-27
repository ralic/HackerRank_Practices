package org.raliclo.apache.mapreduce_Q4;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class Q4Partitioner extends Partitioner<Text, Text> {


    @Override
    public int getPartition(Text year, Text data, int numberOfTasks) {

//        if (year.toString().equals("2010")) {
//            return 1;
//        } else if (year.toString().equals("2012")) {
//            return 2;
//        } else {
//            return 0;
//        }
        return Integer.parseInt(year.toString()) % numberOfTasks;
    }
}

