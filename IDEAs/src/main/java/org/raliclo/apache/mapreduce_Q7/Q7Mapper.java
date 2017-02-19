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
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;


public class Q7Mapper extends
        Mapper<LongWritable, Text, Text, Text> {

    ArrayList<String> input;
    int lines = 0;
    String[] goldenData;
    int[] finalRecord;
    // 0 = unkonwn, -1 = NaN , -2 = difference , n = repeating

    {
        /*
            Load smaller file as data for merging.
         */
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            Path hdfile = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/format/" +
                    "secom_labels.data");
            /*
                Store smaller data set in a ArrayList (input)
             */
            FSDataInputStream fsIntputStream = fs.open(hdfile);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(fsIntputStream));
            input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
            reader.close();
            System.out.println();
//            System.out.println(fsIntputStream.readUTF());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void setup(Context context) {

    }

    @Override
    protected void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {


        String newLine = input.get(lines) + " " + value;

        if (lines == 0) {
            goldenData = newLine.split(" ");
            finalRecord = new int[goldenData.length];
        } else {
            arrayCheck(newLine.split(" "));
        }

        context.write(new Text(String.valueOf(lines)), new Text(newLine));
        lines++;
    }


    @Override
    protected void cleanup(Context context) throws IOException {
        recordWriter();
    }

    protected void arrayCheck(String[] newLineArray) {
        for (int i = 0; i < newLineArray.length; i++) {
            if (finalRecord[i] >= 0) {
                if (newLineArray[i].equals("NaN")) {
                    finalRecord[i] = -1;
                    break;
                } else if (!goldenData[i].equals(newLineArray[i])) {
                    finalRecord[i] = -2;
                    break;
                } else {
                    finalRecord[i]++;
                    break;
                }
            }
        }
    }

    protected void recordWriter() throws IOException {
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);
        Path fileRecords = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/records/" +
                "fileRecords.txt");
        FSDataOutputStream touchFile = fs.create(fileRecords);
        touchFile.close();
        for (int i = 0; i < finalRecord.length; i++) {
            if (finalRecord[i] == -1 || finalRecord[i] == lines) {
                Files.write(Paths.get(fileRecords.toString()),
                        "0 ".getBytes(), // Delete
                        StandardOpenOption.APPEND);
            } else {
                Files.write(Paths.get(fileRecords.toString()),
                        "1 ".getBytes(), // Keep
                        StandardOpenOption.APPEND);
            }
        }

    }
};
