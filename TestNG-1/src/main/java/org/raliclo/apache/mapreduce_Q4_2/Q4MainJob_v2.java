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

public class Q4MainJob_v2 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {

        if (args.length == 0) {
            args = new String[2];
            args[0] = Paths.get("").toAbsolutePath().toString()
                    .concat("/src/main/java/org/raliclo/apache/mapreduce_Q4_2/input");
            args[1] = Paths.get("").toAbsolutePath().toString()
                    .concat("/src//main/java/org/raliclo/apache/mapreduce_Q4_2/output");
        }

        java.nio.file.Path path = Paths.get(args[1]);
        if (Files.exists(path)) {
            FileUtils.deleteDirectory(path.toFile());
        }

        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf, "Q4MainJob");

        job.setNumReduceTasks(3);
        job.setJarByClass(Q4MainJob_v2.class);
        job.setMapperClass(Q4Mapper_v2.class);
        job.setReducerClass(Q4Reducer_v2.class);

        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        System.exit(job.waitForCompletion(true) ? 0 : 1);

    }

}
