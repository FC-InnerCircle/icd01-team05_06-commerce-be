package com.commerce.service.order.application.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class ProductNotFoundException(productId: Long) :
    CustomException(
        HttpStatus.NOT_FOUND,
        ErrorCode.ORDER_NOT_FOUND.apply { this.message = "Product not found: $productId" }
    )