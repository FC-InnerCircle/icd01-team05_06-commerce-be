package com.commerce.service.auth.controller.request

import com.commerce.service.auth.application.usecase.command.SignUpCommand

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
) {
    fun toCommand() = SignUpCommand(
        email = email,
        password = password,
        name = name,
        phone = phone
    )
}
