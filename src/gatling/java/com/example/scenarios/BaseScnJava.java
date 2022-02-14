package com.example.scenarios;

import com.example.data.RandomDataJava;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseScnJava extends RandomDataJava {

    public Map<String, String> defaultHeaders = new HashMap<>();

    public BaseScnJava(){
        defaultHeaders.put("Content Type", "application/json; charset=utf8");
        defaultHeaders.put("Accept", "application/json");
    }


}
