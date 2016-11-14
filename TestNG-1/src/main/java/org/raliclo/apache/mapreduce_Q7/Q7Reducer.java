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
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Q7Reducer extends
        Reducer<Text, Text, Text, Text> {

    static Configuration conf = new Configuration();
    //        conf.set("fs.default.name", "hdfs://localhost:9000");
    // Print Yield Fails
    static Path yieldLossFile = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/records/" +
            "Yield_Fails.txt");
    static Path monthLossFile = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/records/" +
            "Monthly_Yield_Fails.txt");
    static HashMap<String, Integer> freqhashMap = new HashMap();
    static String[] flags;

    {
        try {
            FileSystem fs = FileSystem.get(conf);
            FSDataOutputStream fsOutStream = fs.create(yieldLossFile);
            fsOutStream.close();

            FSDataOutputStream fsOutStream2 = fs.create(monthLossFile);
            fsOutStream2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    {
        try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            Path fileRecords = new Path("./src/main/java/org/raliclo/apache/mapreduce_Q7/records/" +
                    "fileRecords.txt");
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            Files.newInputStream(Paths.get(fileRecords.toString()),
                                    StandardOpenOption.READ)));
            ArrayList<String> input = reader.lines().collect(Collectors.toCollection(ArrayList::new));
            reader.close();
            flags = input.get(0).split(" ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void reduce(Text lineN, Iterable<Text> datas,
                          Context context) throws IOException, InterruptedException {

        for (Text item : datas) {
            String[] data = item.toString().split(" ");
            String postData = "";
            for (int i = 0; i < data.length; i++) {
                if (flags[i].equals("1")) {
                    postData += (data[i] + " ");
                }
            }
            /*
                Output as yieldLossFile
             */
//            System.out.println(data[0]);

            if (data[0].equals("1")) {
                byte[] dataBytes = (item.toString() + "\n").getBytes();
//                fsOutStream.write(dataBytes);
                Files.write(Paths.get(yieldLossFile.toString()),
                        dataBytes,
                        StandardOpenOption.APPEND);

                String regex = "\"[0-9]{2}/([0-9]{2})/[0-9]{4}";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(data[1]);
                while (matcher.find()) {
                    String month = matcher.group(1);
//                    System.out.println(month);
                    if (freqhashMap.containsKey(month)) {
                        freqhashMap.put(month, freqhashMap.get(month) + 1);
                    } else {
                        freqhashMap.put(month, 1);
                    }
                }
            }
            /*
                Fill in data back to stream.
             */
            context.write(lineN, new Text(postData));
        }
    }

    @Override
    protected void cleanup(Context context) {
        /*
            Generate Monthly Yield Loss File
         */
        freqhashMap.forEach((k, v) -> {
            byte[] dataBytes = (k.toString() + " " + v.toString() + "\n").getBytes();
            try {
                Files.write(Paths.get(monthLossFile.toString()),
                        dataBytes,
                        StandardOpenOption.APPEND);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}

