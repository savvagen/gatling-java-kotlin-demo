package com.example.modelds

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude


@JsonIgnoreProperties("id")
@JsonInclude(JsonInclude.Include.NON_NULL)
data class Comment(
    private val id: Int? = null,
    private val post: Int,
    private val name: String,
    private val email: String,
    private val likes: MutableList<Int>,
    private val dislikes: MutableList<Int>,
    private val body: String,
    private val createdAt: String): Model
