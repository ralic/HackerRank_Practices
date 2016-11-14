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

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Q5Mapper extends
        Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] words = value.toString().split(",");
        String year = words[0];
        if (!year.equals("YEAR")) {
            //YEAR,DEPT,COURSE,STUDENT,SCORE1,SCORE2,SCORE3
            // 0    1     2      3      4       5      6
            int sum = 0;
            double avg;

            for (int i = 4; i < 7; i++) {
                sum += Integer.parseInt(words[i]);
            }

            avg = sum / 3.0;
            Text mapV = new Text(words[2] + "_" + avg + "_" + words[0] + "_" + words[1]);
            context.write(new Text(words[3]), mapV);
        }
    }
}

