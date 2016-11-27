package org.raliclo.apache.mapreduce_Q6.Extra;/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;


public class Q6MyValue extends DoubleWritable {

    private Text value;

    public Q6MyValue() {
        this.value = new Text();
    }

    public Q6MyValue(Text value) {

        this.value = value;

    }

    public void readFields(DataInput in) throws IOException {

        value.readFields(in);

    }

    public void write(DataOutput out) throws IOException {

        value.write(out);

    }


    public Text getValue() {

        return value;

    }

    public void setValue(Text value) {

        this.value = value;

    }

}