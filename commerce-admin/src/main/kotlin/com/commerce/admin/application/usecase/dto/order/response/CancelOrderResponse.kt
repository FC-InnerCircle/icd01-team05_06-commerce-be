package com.commerce.admin.application.usecase.dto.order.response

import java.time.LocalDateTime

data class CancelOrderResponse(
    val id: String,
    val status: String,
    val cancelledAt: LocalDateTime,
    val reason: String,
)
