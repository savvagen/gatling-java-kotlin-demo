package com.example.modelds

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude

@JsonIgnoreProperties("id")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Post(
    val id: Int? = null,
    val title: String,
    val subject: String,
    val body: String,
    val category: String,
    val user: Int,
    val comments: MutableList<Int>,
    val createdAt: String): Model {

        fun addComments(vararg ids: Int): Post {
            ids.forEach(comments::add)
            return this;
        }


    }