/*
 * Info: Name=Lo,WeiShun 
 * Author: raliclo
 * Filename: PersonCollectorTest.java
 * Date and Time: Jul 17, 2016 2:03:46 AM
 * Project Name: TestNG-1
 */
 /*
 * Copyright 2016 raliclo.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package Java8_Tests.ClassTest;

import Java8_Tests.ClassTest.Person;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//import Java8_Tests.ClassTests.Person;   // Not Necessary if it's in same package.

public class TestNG_PersonTest {

    List<Person> persons = Arrays.asList(
            new Person("raliclo1", "new york"),
            new Person("raliclo", "chicago"),
            new Person("raliclo3", "new york"),
            new Person("raliclo", "chicago")
    );

    @Test(description = "Collect, with groupingB")
    public void collect0() {
        Map<String, List<Person>> collect = persons.stream().collect(
                Collectors.groupingBy(Person::getCity));
        System.out.println(collect);
        Assert.assertEquals(collect.containsKey("chicago"), true);
        Assert.assertEquals(collect.containsKey("new york"), true);
    }

    //multi-level reduction, toSet() ---> for removing duplicates.
    @Test(description = "Collect by City, Use Collectors.toSet()")
    public void collect1() {
        Map<String, Set<String>> collect1
                = persons.stream().collect(
                Collectors.groupingBy(Person::getCity,
                        Collectors.mapping(Person::getName, Collectors.toSet())));
        System.out.println(collect1);
        Assert.assertEquals(collect1.toString(), "{chicago=[raliclo], new york=[raliclo1, raliclo3]}");
    }

    //multi-level reduction, toList() ---> for whole list.
    @Test(description = "Collect by City, Use Collectors.toList()")
    public void collect2() {
        Map<String, List<String>> collect2
                = persons.stream().collect(
                Collectors.groupingBy(
                        Person::getCity,
                        Collectors.mapping(Person::getName, Collectors.toList())
                )
        );
        System.out.println(collect2);
        Assert.assertEquals(collect2.toString(), "{chicago=[raliclo, raliclo], new york=[raliclo1, raliclo3]}");
    }

    @Test(description = "Collect by Name")
    public void collect3() {
        Map<String, List<Person>> collect = persons.stream().collect(
                Collectors.groupingBy(Person::getName));
        System.out.println(collect);
//        Assert.assertEquals(true, collect.containsKey("chicago"));
//        Assert.assertEquals(true, collect.containsKey("new york"));
    }
}
