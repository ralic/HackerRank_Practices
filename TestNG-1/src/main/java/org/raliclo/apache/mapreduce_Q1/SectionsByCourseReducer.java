package org.raliclo.apache.mapreduce_Q1;

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
        for (Text insideCS : courseScore) {
            finalValue = finalValue
                    .concat(insideCS.toString())
                    .concat(",");
            String[] splitedCS = insideCS.toString().split("[()]");
            String courseName = splitedCS[0];
            int score = Integer.parseInt(splitedCS[1]);
            sumScore += score;
        }
        int count = 0;
        for (int i = 0; i < finalValue.length(); i++) {
            if (finalValue.charAt(i) == ',') {
                count++;
            }
        }
        context.write(
                new Text(studentName + "(" + sumScore / count + ")"),
                new Text(finalValue));
    }
}
