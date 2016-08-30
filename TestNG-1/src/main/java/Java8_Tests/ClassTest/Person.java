/*
 * Info: Name=Lo,WeiShun 
 * Author: raliclo
 * Filename: Person.java
 * Date and Time: Jul 17, 2016 1:57:26 AM
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



/**
 * @author raliclo
 */
public class Person {

    private String name;
    private String city;

    //getters, setters, constructor, toString 
    public Person(String name, String city) {
        this.name = name;
        this.city = city;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }
}
