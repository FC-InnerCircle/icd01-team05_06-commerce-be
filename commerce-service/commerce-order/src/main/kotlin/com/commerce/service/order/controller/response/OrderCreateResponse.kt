package com.commerce.service.order.controller.response

import java.math.BigDecimal

data class OrderCreateResponse(
    val id: Long,
    val orderNumber: String,
    val orderStatus: String,
    val orderDate: String,
    val products: List<ProductSummary>,
) {
    data class ProductSummary(
        val id: Long,
        val name: String,
        val quantity: Int,
        val price: BigDecimal,
        val discountedPrice: BigDecimal
    )
}