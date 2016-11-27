//package Cucumber_Steps_Java8Test;/**
// * Created by raliclo on 9/1/16.
// * Project Name : TestNG-1
// */
//
//import cucumber.api.java.en.Given;
//import cucumber.api.java8.En;
//
//
//public class Cucumber_Steps implements En {
//    // Java 8
//    public Cucumber_Steps() {
//        Given("I have (\\d+) cukes in my belly",
//                (Integer cukes) -> {
//                    System.out.format("Cukes: %n\n", cukes);
//                });
//    }
//
//    // Java 6
//    @Given("I have (\\d+) cukes in my belly")
//    public void I_have_cukes_in_my_belly(int cukes) {
//        System.out.format("Cukes: %n\n", cukes);
//    }
//}
