package com.commerce.service.auth.application.usecase.dto

data class TokenInfoDto(
    val accessToken: String,
    val accessTokenExpiresIn: Long,
    val refreshToken: String,
    val refreshTokenExpiresIn: Long
)