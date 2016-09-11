package org.raliclo.WordCountWeb.RestAPI;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Spring_WordCountSite {

    public static void main(String[] args) throws Exception {
         /*
         * Running Spring Spring Application and Autowire to Instance of Service Objects
         * */
        SpringApplication.run(Spring_WordCountSite.class, args);
    }
}