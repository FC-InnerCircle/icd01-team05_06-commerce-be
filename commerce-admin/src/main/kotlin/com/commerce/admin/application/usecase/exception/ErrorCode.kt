package com.commerce.admin.application.usecase.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail

/**
 * 에러 코드 정의
 *
 * 주문 관련 에러 (O001 ~ O005):
 * - O001: 주문을 찾을 수 없는 경우
 * - O002: 잘못된 주문 상태
 * - O003: 이미 취소된 주문
 * - O004: 취소할 수 없는 주문 상태
 * - O005: 잘못된 주문 업데이트 요청
 *
 * 제품 관련 에러 (P001 ~ P002):
 * - P001: 제품을 찾을 수 없는 경우
 * - P002: 제품 재고 부족
 *
 * 결제 관련 에러 (PM001 ~ PM003):
 * - PM001: 결제 처리 실패
 * - PM002: 잘못된 결제 방법
 * - PM003: 이미 처리된 결제
 *
 * 배송 관련 에러 (S001 ~ S002):
 * - S001: 배송 불가능한 주소
 * - S002: 잘못된 배송 방법
 *
 * 재고 관련 에러 (I001 ~ I002):
 * - I001: 재고 부족
 * - I002: 재고 업데이트 실패
 *
 * 주문 내보내기 관련 에러 (E001 ~ E002):
 * - E001: 내보내기 작업을 찾을 수 없음
 * - E002: 내보내기 처리 실패
 *
 * 레이트 리미팅 에러 (R001):
 * - R001: 요청 한도 초과
 */
enum class ErrorCode(
    val code: String,
    val message: String,
    val httpStatus: HttpStatus,
) {
    // General errors
    INVALID_INPUT("C001", "Invalid input provided", HttpStatus.BAD_REQUEST),
    RESOURCE_NOT_FOUND("C002", "Requested resource not found", HttpStatus.NOT_FOUND),
    INTERNAL_SERVER_ERROR("C003", "Internal server error occurred", HttpStatus.INTERNAL_SERVER_ERROR),

    // Order related errors
    ORDER_NOT_FOUND("O001", "Order not found", HttpStatus.NOT_FOUND),
    INVALID_ORDER_STATUS("O002", "Invalid order status", HttpStatus.BAD_REQUEST),
    ORDER_ALREADY_CANCELLED("O003", "Order is already cancelled", HttpStatus.CONFLICT),
    ORDER_NOT_CANCELLABLE("O004", "Order cannot be cancelled in its current state", HttpStatus.CONFLICT),
    INVALID_ORDER_UPDATE("O005", "Invalid order update request", HttpStatus.BAD_REQUEST),

    // Product related errors
    PRODUCT_NOT_FOUND("P001", "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_OUT_OF_STOCK("P002", "Product is out of stock", HttpStatus.CONFLICT),

    // Payment related errors
    PAYMENT_FAILED("PM001", "Payment processing failed", HttpStatus.PAYMENT_REQUIRED),
    INVALID_PAYMENT_METHOD("PM002", "Invalid payment method", HttpStatus.BAD_REQUEST),
    PAYMENT_ALREADY_PROCESSED("PM003", "Payment has already been processed", HttpStatus.CONFLICT),

    // Shipping related errors
    SHIPPING_NOT_AVAILABLE("S001", "Shipping is not available to the specified address", HttpStatus.CONFLICT),
    INVALID_SHIPPING_METHOD("S002", "Invalid shipping method", HttpStatus.BAD_REQUEST),

    // Inventory related errors
    INSUFFICIENT_INVENTORY("I001", "Insufficient inventory for the requested product(s)", HttpStatus.CONFLICT),
    INVENTORY_UPDATE_FAILED("I002", "Failed to update inventory", HttpStatus.INTERNAL_SERVER_ERROR),

    // Export related errors
    EXPORT_NOT_FOUND("E001", "Export not found", HttpStatus.NOT_FOUND),
    EXPORT_FAILED("E002", "Order export process failed", HttpStatus.INTERNAL_SERVER_ERROR),

    // Rate limiting errors
    RATE_LIMIT_EXCEEDED("R001", "Rate limit exceeded", HttpStatus.TOO_MANY_REQUESTS),
    ;

    fun toProblemDetail(): ProblemDetail =
        ProblemDetail.forStatusAndDetail(httpStatus, message).apply {
            setProperty("errorCode", code)
        }
}
