package com.iti.mongez.domain.auth.model

data class User(
    val id: String,
    val firstName: String,
    val email: String,
    val token: String
)
