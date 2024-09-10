package com.commerce.service.auth.application.usecase.dto

data class MemberInfoDto(
    val id: Long,
    val email: String,
    val name: String,
    val phone: String,
)