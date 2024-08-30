package com.commerce.admin.application.usecase.dto.order.response

import java.time.LocalDateTime

data class UpdateStatusResponse(
    val id: String,
    val status: String,
    val updatedAt: LocalDateTime,
)
