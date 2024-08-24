package com.commerce.service.auth.application.usecase.command

data class SignInCommand(
    val email: String,
    val password: String,
)
