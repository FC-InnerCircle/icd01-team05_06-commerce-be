package com.commerce.common.jwt.application.usecase

data class TokenDto(
    val token: String,
    val expiresIn: Long
)
