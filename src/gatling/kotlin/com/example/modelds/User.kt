package com.example.modelds

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonIgnoreProperties("id")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class User (val id: Int? = null,
                 val name: String,
                 val username: String,
                 val email: String,
                 val createdAt: String): Model