package com.commerce.service.auth.controller.request

import com.commerce.service.auth.application.usecase.command.LoginCommand

data class LoginRequest(
    val email: String,
    val password: String,
) {
    fun toCommand() = LoginCommand(
        email = email,
        password = password
    )
}
