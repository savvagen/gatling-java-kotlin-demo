package com.example.data;

import com.example.javaModels.User;
import com.github.javafaker.Faker;
import com.google.gson.Gson;
import io.gatling.javaapi.core.Session;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.function.Function;

public final class JavaTemplates {

    private static final Faker faker = new Faker(new Locale("en_US"));
    private static final Gson gson = new Gson();

    public final static Function<Session, String> userTemplate = session -> {
        var fakeUser = User.builder().withName(faker.name().firstName() + " " + faker.name().lastName())
                .withEmail(faker.internet().emailAddress())
                .withUsername(faker.name().username())
                .withCreatedAt(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
                .build();
        return gson.toJson(fakeUser);
    };

}
