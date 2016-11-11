package org.raliclo.apache.mapreduce_Q7;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

public class Q7MySorting extends WritableComparator {
    protected Q7MySorting() {
        super(Text.class, true);
    }


// BUS,BUS532,Lily,5	7.8


    @SuppressWarnings("rawtypes")
    @Override
    public int compare(WritableComparable w1, WritableComparable w2) {
        String[] k1 = w1.toString().split(",");
        String[] k2 = w2.toString().split(",");

        // k[1] courseName
        // k[2] studentName
        if (k1[2].compareTo(k2[2]) > 0) {
            return 1;
        } else if (k1[2].compareTo(k2[2]) < 0) {
            return -1;
        } else {
            return k1[1].compareTo(k2[1]);
        }

    }
}