package com.example.simulations;

import io.gatling.javaapi.core.Simulation;

import java.util.Optional;

abstract public class BaseSimulation extends Simulation {


    public static String getProperty(String propertyName, String defaultValue){
        return Optional.ofNullable(System.getenv(propertyName))
                .orElse(defaultValue);
    }


}
