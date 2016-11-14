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

package org.raliclo.apache.mapreduce_Q5;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.file.Paths;

public class Q5Reducer extends
        Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text name, Iterable<Text> datas,
                          Context context) throws IOException, InterruptedException {

        /*
            Access the file system
         */
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        int count = 0;
        double sumScore = 0;

        /*
            Write the Data Stream.
         */
        for (Text item : datas) {

        /*
            Get the file system's path &
            Create Corresponding DataStream to hadoop file system.
         */
            //        System.out.println(fs.getUri());
            //Peter	CS557_8.333333333333334_2015_MATH
            //        0      1               2    3
            String[] inItems = item.toString().split("_");
            String data = name.toString() + "\t" + inItems[0] + "\t" + inItems[1] + "\n";
            count++;
            sumScore += Double.parseDouble(inItems[1]);
            org.apache.hadoop.fs.Path hdfile = (org.apache.hadoop.fs.Path) Paths.get("./src/main/java/org/raliclo/apache/mapreduce_Q5/output/" +
                    inItems[3] + "_Dept.txt");
//            Path netfile = Paths.get("./src/main/java/org/raliclo/apache/mapreduce_Q5/output/" +
//                    inItems[3] + "_Dept.txt");

            FSDataOutputStream fsOutStream;
            if (!fs.exists(hdfile)) {
                fsOutStream = fs.create(hdfile);
                fsOutStream.writeUTF("Student\tCourse\tAvgScore\n");
                fsOutStream.writeUTF(data);
                fsOutStream.close();
            } else {
                fsOutStream = fs.append(hdfile);
                byte[] dataBytes = (data.toString()).getBytes();
                fsOutStream.write(dataBytes, 0, dataBytes.length);
                fsOutStream.close();
                System.out.println(fs.getFileStatus(hdfile));
            }
        }
//        fsOutStream.writeUTF("------\t------\t-------\n");
//        fsOutStream.writeUTF("Overall\t      \t" + (sumScore / count) + "\n");
    }
}

