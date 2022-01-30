package com.example.javaModels;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"id"})
@Data
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class User {

    private Integer id;
    private String name;
    private String username;
    private String email;
    private String createdAt;

}
