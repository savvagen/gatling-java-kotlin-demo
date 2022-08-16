package com.example.javaModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"id"})
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Post {

    private int id;
    private String title;
    private String subject;
    private String body;
    private String category;
    private int user;
    private List<Integer> comments;
    private String createdAt;

    public Post addComments(Integer... commentIds){
        this.comments.addAll(Arrays.asList(commentIds));
        return this;
    }

}

