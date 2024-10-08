package com.commerce.common.response

enum class ErrorCode(val code: String, var message: String) {
    INVALID_INPUT("E001", "Invalid input parameter"),
    ORDER_NOT_FOUND("E002", "Order not found"),
    DUPLICATED_EMAIL("E003", "중복된 이메일입니다."),
    LOGIN_FAILED("E004", "아이디 혹은 비밀번호가 일치하지 않습니다."),
    INSUFFICIENT_STOCK("E005", "Insufficient stock for products"),
    ORDER_CREATION_FAILED("E006", "Order creation failed"),
    SHOPPING_CART_NOT_FOUND("E101", "장바구니 상품이 존재하지 않습니다."),
    PERMISSION_ERROR("E403", "Permission Denied"),
    INTERNAL_SERVER_ERROR("E999", "Internal server error")
}