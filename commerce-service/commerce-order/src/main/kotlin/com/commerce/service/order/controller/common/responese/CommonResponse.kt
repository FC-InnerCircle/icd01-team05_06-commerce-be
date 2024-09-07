package com.commerce.service.order.controller.common.responese

import com.commerce.service.order.applicaton.usecase.exception.ErrorResponse

data class CommonResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: List<ErrorResponse>? = null,
)