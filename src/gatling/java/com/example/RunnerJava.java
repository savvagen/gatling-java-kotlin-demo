package com.example;

import com.example.simulations.JavaSimulation;
import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;

public class RunnerJava {

    public static void main(String[] args) {

        var props = new GatlingPropertiesBuilder();
        props.simulationClass(JavaSimulation.class.getName());

        Gatling.fromMap(props.build());

    }
}
