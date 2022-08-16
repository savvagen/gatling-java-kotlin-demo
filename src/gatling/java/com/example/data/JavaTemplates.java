package com.example.data;

import com.example.javaModels.Post;
import com.example.javaModels.User;
import io.gatling.javaapi.core.Session;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public final class JavaTemplates extends RandomDataJava {

    public final static Function<Session, String> userTemplate = session -> {
        var fakeUser = User.builder().withName(faker.name().firstName() + " " + faker.name().lastName())
                .withEmail(faker.internet().emailAddress())
                .withUsername(faker.name().username())
                .withCreatedAt(OffsetDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
                .build();
        return gson.toJson(fakeUser);
    };

    public final static Function<Session, String> postTemplate = session -> {
        var fakePost = Post.builder()
                .withTitle(String.format("My Fake post from %s", dateTimeNow()))
                .withSubject("Performance Testing")
                .withBody(faker.lorem().sentence())
                .withCategory(randomCategory())
                .withUser(session.getInt("userId"));
        return gson.toJson(fakePost);
    };

}
