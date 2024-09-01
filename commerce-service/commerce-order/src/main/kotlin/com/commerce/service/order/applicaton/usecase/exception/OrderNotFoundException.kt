package com.commerce.service.order.applicaton.usecase.exception

class OrderNotFoundException(orderId: String) :
    CustomException(ErrorCode.ORDER_NOT_FOUND.apply { this.message = "Order not found: $orderId" })