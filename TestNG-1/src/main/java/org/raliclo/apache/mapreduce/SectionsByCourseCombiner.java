package org.raliclo.apache.mapreduce;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SectionsByCourseCombiner extends
        Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text studentName, Iterable<Text> courseScore,
                          Context context) throws IOException, InterruptedException {

        String finalValue = "";
        double sumScore = 0;
        for (Text insideCS : courseScore) {
            finalValue = finalValue
                    .concat(insideCS.toString())
                    .concat(",");
            String[] splitedCS = insideCS.toString().split("[()]");
            String courseName = splitedCS[0];
            int score = Integer.parseInt(splitedCS[1]);
            sumScore += score;
        }
//        context.write(, new Text());
        context.write(
                new Text(studentName),
                new Text(finalValue + " " + sumScore));
//        context.write(token, new IntWritable(sum));
    }
}
