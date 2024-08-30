package com.commerce.admin.application.usecase.dto.order.response

import java.time.LocalDateTime

data class ExportOrdersResponse(
    val exportId: String,
    val status: String,
    val estimatedCompletionTime: LocalDateTime,
)
