/*
 * Copyright 2016 Ralic Lo<raliclo@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

package org.raliclo.apache.mapreduce_Q7;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Q7Mapper extends
        Mapper<LongWritable, Text, Text, Text> {

    static Path hdfile;
    static ArrayList<String> input;
    int lines = 0;

    {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            hdfile = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/format/" +
                    "secom_labels.data");
            FSDataInputStream fsIntputStream = fs.open(hdfile);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(fsIntputStream.getWrappedStream()));
            input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
            reader.close();
//            System.out.println(fsIntputStream.readUTF());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

//        System.out.println(input.get(lines));
//        System.out.println();
        context.write(new Text(String.valueOf(lines)), new Text(input.get(lines) + " " + value));
        lines++;
    }
}

