package com.commerce.service.auth.application.usecase.dto

data class LoginInfoDto(
    val memberInfo: LoginMemberInfoDto,
    val tokenInfo: LoginTokenInfoDto
)

data class LoginMemberInfoDto(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String,
)

data class LoginTokenInfoDto(
    val accessToken: String,
    val refreshToken: String
)