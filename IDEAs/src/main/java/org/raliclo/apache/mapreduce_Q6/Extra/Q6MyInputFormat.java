package org.raliclo.apache.mapreduce_Q6.Extra;/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;

import java.io.IOException;

public class Q6MyInputFormat extends FileInputFormat<Q6MyKey, Q6MyValue> {

    @Override

    public RecordReader<Q6MyKey, Q6MyValue> createRecordReader(InputSplit arg0,
                                                               TaskAttemptContext arg1) throws IOException, InterruptedException {

        return new Q6MyRecordReader();

    }

}

