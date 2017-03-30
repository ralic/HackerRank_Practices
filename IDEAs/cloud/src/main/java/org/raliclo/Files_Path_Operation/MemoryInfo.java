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
package org.raliclo.Files_Path_Operation;/**
 * Created by raliclo on 9/9/16.
 * Project Name : TestNG-1
 */

import java.util.ArrayList;

public class MemoryInfo {

    public static void topten() {
        System.out.println();
        ArrayList<String> ans = (ArrayList<String>) RunCommand.runexec("top -l 1 -s 0");
        int i = 0;
        while (i++ < 10) {
            System.out.println(ans.get(i));
        }
    }

    public static void main(String[] args) {
        topten();
    }
}
