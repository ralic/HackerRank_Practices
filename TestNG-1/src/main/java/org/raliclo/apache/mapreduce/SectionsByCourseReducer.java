package org.raliclo.apache.mapreduce;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SectionsByCourseReducer extends
        Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text studentName, Iterable<Text> courseScore,
                          Context context) throws IOException, InterruptedException {

        String finalValue = "";
        double sumScore = 0;
        String[] data = new String[2];
        for (Text insideCS : courseScore) {
            data = insideCS.toString().split(" ");
            finalValue = finalValue.concat(data[0]);
        }
        sumScore = Double.parseDouble(data[1]);
        int count = 0;
        for (int i = 0; i < finalValue.length(); i++) {
            if (finalValue.charAt(i) == ',') {
                count++;
            }
        }

        context.write(
                new Text(studentName + "(" + sumScore / count + ")"),
                new Text(finalValue));
        //count = 0;

//        context.write(token, new IntWritable(sum));
    }
}
