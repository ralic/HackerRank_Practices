package org.raliclo.apache.mapreduce_Q6;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

public class Q6MySorting extends WritableComparator {
    protected Q6MySorting() {
        super(Text.class, true);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        String k1 = w1.toString();
        String k2 = w2.toString();

        int result = k1.compareTo(k2);
        if (0 == result) {
            result = -1 * k1.compareTo(k2);
        }
        return result;
    }
}