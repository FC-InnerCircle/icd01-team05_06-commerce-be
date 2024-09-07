package com.commerce.service.order.applicaton.usecase.exception

class InvalidInputException(message: String? = null):
        CustomException(ErrorCode.INVALID_INPUT.apply { this.message = message ?: "Invalid input" })