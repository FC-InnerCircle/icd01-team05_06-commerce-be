package com.commerce.service.order.applicaton.usecase.exception

import com.commerce.common.response.CustomException
import com.commerce.common.response.ErrorCode
import org.springframework.http.HttpStatus

class OrderNotFoundException(orderId: String) :
    CustomException(
        HttpStatus.NOT_FOUND,
        ErrorCode.ORDER_NOT_FOUND.apply { this.message = "Order not found: $orderId" }
    )