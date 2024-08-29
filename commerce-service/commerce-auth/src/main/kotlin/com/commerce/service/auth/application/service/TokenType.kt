package com.commerce.service.auth.application.service

import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

enum class TokenType(
    val id: Int,
    val description: String,
    val validTime: Duration
) {
    ACCESS_TOKEN(
        id = 1,
        description = "Access Token",
        validTime = 2.hours
    ),
    REFRESH_TOKEN(
        id = 2,
        description = "Refresh Token",
        validTime = 14.days
    )
}
