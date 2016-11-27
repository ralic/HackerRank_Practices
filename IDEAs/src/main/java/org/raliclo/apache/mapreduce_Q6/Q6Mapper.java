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

package org.raliclo.apache.mapreduce_Q6;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;


public class Q6Mapper extends
        Mapper<LongWritable, Text, Text, Text> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String[] words = value.toString().split(",");
        String year = words[0];
        if (!year.equals("YEAR")) {
            //YEAR,DEPT,COURSE,STUDENT,SCORE1,SCORE2,SCORE3,SCORE4,SCORE5
            // 0    1     2      3      4       5      6     7     8
            Double sum = 0.0;
            Double avg;
            int count = words.length - 4;
            for (int i = 4; i < words.length; i++) {
                sum += Integer.parseInt(words[i]);
            }

            avg = sum / count;
            //Text dept, Text course, Text student, Text count
            String dept = words[1];
            String course = words[2];
            String student = words[3];
            String myValue = dept + "," + course + "," + student + "," + count;
            context.write(new Text(myValue), new Text(avg.toString()));
        }
    }
}

