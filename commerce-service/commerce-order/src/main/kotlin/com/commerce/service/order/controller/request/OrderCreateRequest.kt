package com.commerce.service.order.controller.request

data class OrderCreateRequest(
    val products: List<ProductInfo>,
    val deliveryInfo: DeliveryInfo,
    val paymentInfo: PaymentInfo
) {
    data class ProductInfo(
        val id: Long,
        val quantity: Int
    )

    data class DeliveryInfo(
        val recipient: String,
        val streetAddress: String,
        val detailAddress: String
    )

    data class PaymentInfo(
        val method: String,
        val cardNumber: String,
        val expirationDate: String,
        val cvv: String
    )
}