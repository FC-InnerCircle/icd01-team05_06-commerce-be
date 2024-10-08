package com.commerce.service.auth.application.usecase.command

import java.math.BigDecimal

data class LoginCommand(
    val email: String,
    val password: String,
)
