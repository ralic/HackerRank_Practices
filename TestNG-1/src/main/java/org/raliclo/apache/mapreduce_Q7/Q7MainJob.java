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

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.nio.file.Files;
import java.nio.file.Paths;

public class Q7MainJob {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            args = new String[3];
            args[0] = Paths.get("").toAbsolutePath().toString()
                    .concat("/src/main/java/org/raliclo/apache/mapreduce_Q7/input");
            args[1] = Paths.get("").toAbsolutePath().toString()
                    .concat("/src//main/java/org/raliclo/apache/mapreduce_Q7/output");
            args[2] = Paths.get("").toAbsolutePath().toString()
                    .concat("/src//main/java/org/raliclo/apache/mapreduce_Q7/records");
        }

        java.nio.file.Path path = Paths.get(args[1]);
        java.nio.file.Path path2 = Paths.get(args[2]);

        if (Files.exists(path)) {
            FileUtils.deleteDirectory(path.toFile());
        }

        if (Files.exists(path2)) {
            FileUtils.deleteDirectory(path2.toFile());
        }

        Configuration conf = new Configuration();
        Job job1 = Job.getInstance(conf, "Q7MainJob");

        job1.setNumReduceTasks(3);
        job1.setJarByClass(Q7MainJob.class);
        job1.setMapperClass(Q7Mapper.class);

//        job1.setPartitionerClass(Q7Partitioner.class);
//        job1.setSortComparatorClass(Q7MySorting.class);
//        job1.setGroupingComparatorClass(Q7MyGrouping.class);
        job1.setReducerClass(Q7Reducer.class);

        job1.setInputFormatClass(TextInputFormat.class);
        job1.setOutputFormatClass(TextOutputFormat.class);

        job1.setOutputKeyClass(Text.class);
        job1.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job1, new Path(args[0]));
        FileOutputFormat.setOutputPath(job1, new Path(args[1]));

        System.exit(job1.waitForCompletion(true) ? 0 : 1);

    }

}
