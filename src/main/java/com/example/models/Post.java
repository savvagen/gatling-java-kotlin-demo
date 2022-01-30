package com.example.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Post {

    int id;
    String title;
    String subject;
    String body;
    String category;
    int user;
    List<Integer> comments = new ArrayList<>();
    String createdAt;

    public Post(int id, String title, String subject, String body, String category, int user, List<Integer> comments, String createdAt) {
        this.id = id;
        this.title = title;
        this.subject = subject;
        this.body = body;
        this.category = category;
        this.user = user;
        this.comments = comments;
        this.createdAt = createdAt;
    }

    public Post() {
    }


    public int getId() {
        return id;
    }

    public Post setId(int id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Post setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getSubject() {
        return subject;
    }

    public Post setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public String getBody() {
        return body;
    }

    public Post setBody(String body) {
        this.body = body;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Post setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getUser() {
        return user;
    }

    public Post setUser(int user) {
        this.user = user;
        return this;
    }

    public List<Integer> getComments() {
        return comments;
    }

    public Post setComments(List<Integer> comments) {
        this.comments = comments;
        return this;
    }

//    public Post setComments(Integer... comments) {
//        this.comments = List.of(comments); // Stream.of(comments).collect(Collectors.toList());
//        return this;
//    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Post setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }
}
