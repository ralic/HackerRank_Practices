package org.raliclo.WordCountWeb.RestAPI;/**
 * Created by raliclo on 9/10/16.
 * Project Name : TestNG-1
 */


import org.raliclo.WordCountWeb.RestAPI.dataApp.WordCountAvro;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Spring_WordCountSite {

    public static void main(String[] args) throws Exception {
        //
        //* Running Spring Spring Application and Autowire to Instance of Service Objects
        //*
        if (args.length == 0) {
            System.out.println("[Usage] No args, App started");
            SpringApplication.run(Spring_WordCountSite.class, args);
        }
        if (args.length >= 1) {
            if (args[0].equals("count")) {
                WordCountAvro.run();
            } else {
                System.out.println("[Usage] extra args must be [count]");
            }
        }
    }
}