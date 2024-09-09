package com.commerce.service.order.applicaton.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class InvalidInputException(message: String? = null) :
    CustomException(
        HttpStatus.BAD_REQUEST,
        ErrorCode.INVALID_INPUT.apply { this.message = message ?: "Invalid input" }
    )