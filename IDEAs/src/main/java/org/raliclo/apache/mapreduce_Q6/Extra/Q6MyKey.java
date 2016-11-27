package org.raliclo.apache.mapreduce_Q6.Extra;/**
 * Created by raliclo on 05/11/2016.
 * Project Name : TestNG-1
 */

import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class Q6MyKey implements WritableComparable {


    private Text dept;
    private Text course;
    private Text studentName;
    private Text count;

    public Q6MyKey() {
        this.dept = new Text();
        this.course = new Text();
        this.studentName = new Text();
        this.count = new Text();
    }

    public Q6MyKey(Text dept, Text course, Text student, Text count) {
        this.dept = dept;
        this.course = course;
        this.studentName = student;
        this.count = count;

    }

    public void readFields(DataInput in) throws IOException {
        dept.readFields(in);
        course.readFields(in);
        studentName.readFields(in);
        count.readFields(in);
    }

    public void write(DataOutput out) throws IOException {
        dept.write(out);
        course.write(out);
        studentName.write(out);
        count.write(out);

    }

    public int compareTo(Object o) {

        Q6MyKey other = (Q6MyKey) o;

        int cmp = studentName.compareTo(other.studentName);
        if (cmp != 0) {
            return cmp;
        }
        cmp = course.compareTo(other.course);

        if (cmp != 0) {
            return cmp;
        }

        return 0;
    }

    public Text getDept() {
        return dept;
    }

    public void setDept(Text dept) {
        this.dept = dept;
    }

    public Text getCourse() {
        return course;
    }

    public void setCourse(Text course) {
        this.course = course;
    }

    public Text getStudentName() {
        return studentName;
    }

    public void setStudentName(Text studentName) {
        this.studentName = studentName;
    }

    public Text getCount() {
        return count;
    }

    public void setCount(Text count) {
        this.count = count;
    }
}

