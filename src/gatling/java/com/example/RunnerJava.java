package com.example;

import com.example.simulations.JavaSimulation;
import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

public class RunnerJava {

    public static void main(String[] args) {

        System.out.println(dateTimeNow());

//        var props = new GatlingPropertiesBuilder();
//        props.simulationClass(JavaSimulation.class.getName());
//
//        Gatling.fromMap(props.build());

    }


    public static String dateTimeNow(){
        return OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
    }
}
