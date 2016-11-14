package org.raliclo.apache.mapreduce_Q6.Extra;/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.input.LineRecordReader;

import java.io.IOException;

public class Q6MyRecordReader extends RecordReader<Q6MyKey, Q6MyValue> {

    private Q6MyKey key;

    private Q6MyValue value;

    private LineRecordReader reader;

    public Q6MyRecordReader() {
        reader = new LineRecordReader();
    }

    @Override

    public void close() throws IOException {

// TODO Auto-generated method stub

        reader.close();

    }

    @Override

    public Q6MyKey getCurrentKey() throws IOException, InterruptedException {

// TODO Auto-generated method stub

        return key;

    }

    @Override

    public Q6MyValue getCurrentValue() throws IOException, InterruptedException {

// TODO Auto-generated method stub

        return value;

    }

    @Override

    public float getProgress() throws IOException, InterruptedException {

// TODO Auto-generated method stub

        return reader.getProgress();

    }

    @Override

    public void initialize(InputSplit is, TaskAttemptContext tac)

            throws IOException, InterruptedException {

        reader.initialize(is, tac);

    }

    @Override

    public boolean nextKeyValue() throws IOException, InterruptedException {

// TODO Auto-generated method stub

        boolean gotNextKeyValue = reader.nextKeyValue();

        if (gotNextKeyValue) {
            if (key == null) {
                key = new Q6MyKey();
            }

            if (value == null) {
                value = new Q6MyValue();
            }
            //YEAR,DEPT,COURSE,STUDENT,SCORE1,SCORE2,SCORE3,SCORE4,SCORE5
            // 0    1     2      3      4       5      6     6      7

            //Text dept, Text course, Text student, Text count

            Text line = reader.getCurrentValue();
            String[] tokens = line.toString().split(",");
            key.setDept(new Text(tokens[1]));
            key.setCourse(new Text(tokens[1]));
            key.setStudentName(new Text(tokens[2]));
            key.setCount(new Text(tokens[2]));
            value.setValue(new Text(tokens[3]));
        } else {
            key = null;
            value = null;

        }

        return gotNextKeyValue;

    }
}