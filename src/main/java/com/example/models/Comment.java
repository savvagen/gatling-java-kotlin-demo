package com.example.models;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"id"})
public class Comment {

    private int id;
    private int post;
    private String name;
    private String email;
    private List<Integer> likes = new ArrayList<>();
    private List<Integer> dislikes;
    private String body;
    private String createdAt;

    public Comment(int id, int post, String name, String email, List<Integer> likes, List<Integer> dislikes, String body, String createdAt) {
        this.id = id;
        this.post = post;
        this.name = name;
        this.email = email;
        this.likes = likes;
        this.dislikes = dislikes;
        this.body = body;
        this.createdAt = createdAt;
    }

    public Comment() {
    }

    public int getId() {
        return id;
    }

    public int getPost() {
        return post;
    }

    public String getEmail() {
        return email;
    }

    public List<Integer> getLikes() {
        return likes;
    }

    public List<Integer> getDislikes() {
        return dislikes;
    }

    public String getBody() {
        return body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Comment setId(int id) {
        this.id = id;
        return this;
    }


    public Comment setPost(int post) {
        this.post = post;
        return this;
    }


    public Comment setEmail(String email) {
        this.email = email;
        return this;
    }


    public Comment setLikes(List<Integer> likes) {
        this.likes = likes;
        return this;
    }


    public Comment setDislikes(List<Integer> dislikes) {
        this.dislikes = dislikes;
        return this;
    }


    public Comment setBody(String body) {
        this.body = body;
        return this;
    }


    public Comment setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public String getName() {
        return name;
    }

    public Comment setName(String name) {
        this.name = name;
        return this;
    }
}
