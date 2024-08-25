package com.commerce.service.auth.application.usecase.command

data class LoginCommand(
    val email: String,
    val password: String,
)
