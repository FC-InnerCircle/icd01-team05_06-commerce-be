package com.commerce.service.order.controller.response

import java.time.LocalDateTime

data class StatusHistoryItem(
    val status: String,
    val timestamp: LocalDateTime
)
