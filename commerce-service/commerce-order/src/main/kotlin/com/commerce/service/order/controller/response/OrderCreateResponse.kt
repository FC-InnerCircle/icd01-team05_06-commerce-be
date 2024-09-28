package com.commerce.service.order.controller.response

import java.time.LocalDateTime

data class OrderCreateResponse(
    val orderId: Long,
    val orderDate: LocalDateTime,
    val totalAmount: Int,
    val status: String,
    val products: List<ProductInfo>,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo
) {
    data class ProductInfo(
        val bookId: Long,
        val title: String,
        val quantity: Int,
        val price: Int
    )

    data class DeliveryInfo(
        val name: String,
        val phoneNumber: String,
        val streetAddress: String,
        val detailAddress: String
    )

    data class PaymentInfo(
        val method: String,
        val lastFourDigits: String
    )
}