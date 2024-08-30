package com.commerce.service.auth.application.usecase.command

data class SignUpCommand(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
)
