package com.example.data;

import com.example.javaModels.Post;
import com.example.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.google.gson.Gson;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RandomDataJava {

    public static Faker faker = new Faker(new Locale("en_US"));
    public static Gson gson = new Gson();
    public static ObjectMapper objectMapper = new ObjectMapper();

    public static DateTimeFormatter isoDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    public static DateTimeFormatter simpleDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String dateTimeNow(){
        return OffsetDateTime.now().format(isoDateFormat);
    }

    public String randomUser(){
        return gson.toJson(new User()
                .setName(faker.name().firstName() + " " + faker.name().lastName())
                .setUsername(faker.name().username())
                .setEmail(faker.internet().emailAddress())
                .setCreatedAt(dateTimeNow())
        );
    }

    public String randomPost(String userEmail){
        return gson.toJson(Post.builder()
                .withTitle("Test Post From " + OffsetDateTime.now() + " " + faker.random().nextInt(100000, 999999))
                .withSubject("Performance Testing")
                .withBody(faker.lorem().sentence())
                .withUser(Integer.parseInt(userEmail))
                .withCreatedAt(dateTimeNow())
                .withComments(List.of(faker.random().nextInt(50, 100), faker.random().nextInt(1, 49)))
        );
    }


}
