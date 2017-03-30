package org.raliclo.apache.mapreduce_Q1;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class SectionsByCourseMapper extends
        Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        if (key.get() >= 0) {
            String[] lines = value.toString().split(",");
            int items = lines.length;
            String courseName = lines[0];
            for (int n = 1; n < items; n++) {
                String[] studentnameScore = lines[n].split("[()]");
                String studentName = studentnameScore[0];
                context.write(new Text(studentnameScore[0]),
                        new Text(courseName + "(" + studentnameScore[1] + ")"));
            }
        }
    }
}
