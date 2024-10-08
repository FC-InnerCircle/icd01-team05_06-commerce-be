package com.commerce.service.order.application.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class OrderCreationException(message: String? = null) : CustomException(
    HttpStatus.BAD_REQUEST,
    ErrorCode.ORDER_CREATION_FAILED.apply { this.message = message ?: "Order creation failed" }
)

