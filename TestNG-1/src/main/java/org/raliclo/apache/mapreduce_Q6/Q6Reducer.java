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

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Q6Reducer extends
        Reducer<Text, Text, Text, Text> {
    @Override
    protected void reduce(Text courseName, Iterable<Text> datas,
                          Context context) throws IOException, InterruptedException {

        int overallCount = 0;
        Double overallSum = 0.0;

        /*
            Write the Data Stream.
         */
        for (Text item : datas) {
            overallCount++;
            overallSum += Double.parseDouble(item.toString());

            /*
            Take out courseName
             */
            int firstCommon = courseName.find(",");

            context.write(new Text(
                    courseName.toString().substring(firstCommon + 1, courseName.getLength())
            ), item);
        }

        context.write(new Text("   Overall Average :" + overallSum / overallCount), new Text("\n"));

    }
}

