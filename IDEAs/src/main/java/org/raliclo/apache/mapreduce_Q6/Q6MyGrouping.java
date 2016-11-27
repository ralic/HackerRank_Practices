package org.raliclo.apache.mapreduce_Q6;/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class Q6MyGrouping extends WritableComparator {
    protected Q6MyGrouping() {
        super(Text.class, true);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        String k1 = w1.toString().split(",")[2];
        String k2 = w2.toString().split(",")[2];

        return k1.compareTo(k2);
    }
}