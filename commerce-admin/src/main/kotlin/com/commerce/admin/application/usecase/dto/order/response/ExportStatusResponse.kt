package com.commerce.admin.application.usecase.dto.order.response

import java.time.LocalDateTime

data class ExportStatusResponse(
    val exportId: String,
    val status: String,
    val downloadUrl: String?,
    val expiresAt: LocalDateTime?,
)
