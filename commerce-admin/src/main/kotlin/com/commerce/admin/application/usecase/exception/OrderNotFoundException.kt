package com.commerce.admin.application.usecase.exception

class OrderNotFoundException(
    message: String? = null,
) : BaseException(ErrorCode.ORDER_NOT_FOUND, message)

// // 주문 관련 예외
// class OrderNotFoundException(message: String? = null) : BaseException(ErrorCode.ORDER_NOT_FOUND, message)
// class InvalidOrderStatusException(message: String? = null) : BaseException(ErrorCode.INVALID_ORDER_STATUS, message)
// class OrderAlreadyCancelledException(message: String? = null) : BaseException(ErrorCode.ORDER_ALREADY_CANCELLED, message)
// class OrderNotCancellableException(message: String? = null) : BaseException(ErrorCode.ORDER_NOT_CANCELLABLE, message)
// class InvalidOrderUpdateException(message: String? = null) : BaseException(ErrorCode.INVALID_ORDER_UPDATE, message)
//
// // 제품 관련 예외
// class ProductNotFoundException(message: String? = null) : BaseException(ErrorCode.PRODUCT_NOT_FOUND, message)
// class ProductOutOfStockException(message: String? = null) : BaseException(ErrorCode.PRODUCT_OUT_OF_STOCK, message)
//
// // 결제 관련 예외
// class PaymentFailedException(message: String? = null) : BaseException(ErrorCode.PAYMENT_FAILED, message)
// class InvalidPaymentMethodException(message: String? = null) : BaseException(ErrorCode.INVALID_PAYMENT_METHOD, message)
// class PaymentAlreadyProcessedException(message: String? = null) : BaseException(ErrorCode.PAYMENT_ALREADY_PROCESSED, message)
//
// // 배송 관련 예외
// class ShippingNotAvailableException(message: String? = null) : BaseException(ErrorCode.SHIPPING_NOT_AVAILABLE, message)
// class InvalidShippingMethodException(message: String? = null) : BaseException(ErrorCode.INVALID_SHIPPING_METHOD, message)
//
// // 재고 관련 예외
// class InsufficientInventoryException(message: String? = null) : BaseException(ErrorCode.INSUFFICIENT_INVENTORY, message)
// class InventoryUpdateFailedException(message: String? = null) : BaseException(ErrorCode.INVENTORY_UPDATE_FAILED, message)
//
// // 주문 내보내기 관련 예외
// class ExportNotFoundException(message: String? = null) : BaseException(ErrorCode.EXPORT_NOT_FOUND, message)
// class ExportFailedException(message: String? = null) : BaseException(ErrorCode.EXPORT_FAILED, message)
//
// // 레이트 리미팅 예외
// class RateLimitExceededException(message: String? = null) : BaseException(ErrorCode.RATE_LIMIT_EXCEEDED, message)
