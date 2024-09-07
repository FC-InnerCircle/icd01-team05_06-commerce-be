package com.commerce.service.order.applicaton.usecase.exception

enum class ErrorCode(val code: String, var message: String) {
    INVALID_INPUT("E001", "Invalid input parameter"),
    ORDER_NOT_FOUND("E002", "Order not found"),
    INTERNAL_SERVER_ERROR("E999", "Internal server error")
}