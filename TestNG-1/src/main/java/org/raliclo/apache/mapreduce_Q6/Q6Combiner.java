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

public class Q6Combiner extends
        Reducer<Text, Text, Text, Text> {

    @Override
    protected void reduce(Text words, Iterable<Text> wordFreq,
                          Context context) throws IOException, InterruptedException {

        // remove duplication of key.
        Integer count = 0;
        for (Text item : wordFreq) {
            count = count + Integer.parseInt(item.toString());
        }

        context.write(words, new Text(count.toString()));
    }
}
