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

package Java8_Tests.ClassTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by raliclo on 9/3/16.
 * Project Name : TestNG-1
 */

public class Permute {
    static void permute(java.util.List<Integer> arr, int k) {
        for (int i = k; i < arr.size(); i++) {
            java.util.Collections.swap(arr, i, k);
            permute(arr, k + 1);
            java.util.Collections.swap(arr, k, i);
        }
        System.out.println(java.util.Arrays.toString(arr.toArray()));
    }

    public static void main(String[] args) {
        Permute.permute(java.util.Arrays.asList(3, 4, 6, 2, 1), 0);
    }
}


