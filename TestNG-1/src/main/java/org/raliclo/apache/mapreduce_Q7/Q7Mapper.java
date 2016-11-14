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
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.nio.file.Paths;


public class Q7Mapper extends
        Mapper<LongWritable, Text, Text, Text> {

    int lines = 0;

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {

        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        org.apache.hadoop.fs.Path hdfile = (org.apache.hadoop.fs.Path)
                Paths.get("./src/main/java/org/raliclo/apache/mapreduce_Q7/format/" +
                        "secom_labels.data");

        FSDataInputStream fsIntputStream;
        if (!fs.exists(hdfile)) {
            fsIntputStream = fs.open(hdfile);
            System.out.println(fsIntputStream.readUTF());

        } else {
            System.out.println("File not existed : " + hdfile);
        }

        lines++;
        context.write(new Text(String.valueOf(lines)), new Text(value));

    }
}

