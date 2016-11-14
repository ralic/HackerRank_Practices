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

package org.raliclo.apache.mapreduce_Q4_2;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Q4Reducer_v2 extends
        Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text year, Iterable<Text> datas,
                          Context context) throws IOException, InterruptedException {

        /*
            Define the file name to be written
         */
        String filePrefix = "Result";

        /*
            Access the file system
         */
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(conf);

        /*
            Get the file system's path
         */
        Path hdfile = new Path("./src//main/java/org/raliclo/apache/mapreduce_Q4_2/output/" +
                filePrefix + "_" + year + ".txt");

        /*
            Create Corresponding DataStream to hadoop file system.
         */
        //        System.out.println(fs.getUri());
        FSDataOutputStream fsOutStream = fs.create(hdfile);

        /*
            Write the Data Stream.
         */
        for (Text item : datas) {
//            fsOutStream.writeUTF(year + " " + item.toString());
//            fsOutStream.writeUTF("hello world");
            byte[] data = (item.toString() + "\n").getBytes();
            fsOutStream.write(data, 0, data.length);
        }

        /*
            Close the Data Stream.
         */
        fsOutStream.close();
    }
}

