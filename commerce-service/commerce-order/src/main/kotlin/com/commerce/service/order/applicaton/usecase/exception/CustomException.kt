package com.commerce.service.order.applicaton.usecase.exception

sealed class CustomException(val errorCode: ErrorCode) : RuntimeException(errorCode.message)

