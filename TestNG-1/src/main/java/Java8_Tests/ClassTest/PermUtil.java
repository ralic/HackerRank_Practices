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
package Java8_Tests.ClassTest;/**
 * Created by raliclo on 7/24/16.
 * Project Name : TestNG-1
 */

// TODO Good for debugging and testing.
import java.util.*;

public class PermUtil <T> {
    private T[] arr;
    private int[] permSwappings;

    public PermUtil(T[] arr) {
        this(arr,arr.length);
    }

    public PermUtil(T[] arr, int permSize) {
        this.arr = arr.clone();
        this.permSwappings = new int[permSize];
        for(int i = 0;i < permSwappings.length;i++)
            permSwappings[i] = i;
    }

    public T[] next() {
        if (arr == null)
            return null;

        T[] res = Arrays.copyOf(arr, permSwappings.length);
        //Prepare next
        int i = permSwappings.length-1;
        while (i >= 0 && permSwappings[i] == arr.length - 1) {
            swap(i, permSwappings[i]); //Undo the swap represented by permSwappings[i]
            permSwappings[i] = i;
            i--;
        }

        if (i < 0)
            arr = null;
        else {
            int prev = permSwappings[i];
            swap(i, prev);
            int next = prev + 1;
            permSwappings[i] = next;
            swap(i, next);
        }

        return res;
    }

    private void swap(int i, int j) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

}