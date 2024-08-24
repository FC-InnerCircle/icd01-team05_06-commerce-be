package com.commerce.common.model.member

import java.time.LocalDateTime

class Member(
    val id: Long = 0,
    val email: String,
    val password: String,
    val name: String,
    val phone: String,
    val lastLoginDate: LocalDateTime? = null,
    val refreshToken: String? = null,
) {
    fun login(refreshToken: String) = Member(
        id = id,
        email = email,
        password = password,
        name = name,
        phone = phone,
        lastLoginDate = LocalDateTime.now(),
        refreshToken = refreshToken,
    )
}