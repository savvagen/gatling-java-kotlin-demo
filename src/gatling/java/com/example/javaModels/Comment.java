package com.example.javaModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties({"id"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@Builder(setterPrefix = "with")
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class Comment {

    private int id;
    private int post;
    private String name;
    private String email;
    private List<Integer> likes;
    private List<Integer> dislikes;
    private String body;
    private String createdAt;

}
