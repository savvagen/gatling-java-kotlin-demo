package com.example;

import com.example.simulations.JavaLoadSimulation;
import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class RunnerJava {

    public static void main(String[] args) {

        var props = new GatlingPropertiesBuilder();
        props.simulationClass(JavaLoadSimulation.class.getName());

        Gatling.fromMap(props.build());

    }

}
