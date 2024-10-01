package com.commerce.service.order.application.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class InsufficientStockException(insufficientProducts: List<Long?>) :
    CustomException(
        HttpStatus.BAD_REQUEST,
        ErrorCode.INSUFFICIENT_STOCK.apply { this.message = "Insufficient stock for products: ${insufficientProducts.joinToString(", ")}" }
    )