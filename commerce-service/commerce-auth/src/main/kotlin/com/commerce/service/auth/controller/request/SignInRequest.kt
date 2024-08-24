package com.commerce.service.auth.controller.request

import com.commerce.service.auth.application.usecase.command.SignInCommand

data class SignInRequest(
    val email: String,
    val password: String,
) {
    fun toCommand() = SignInCommand(
        email = email,
        password = password
    )
}
