package com.commerce.service.order.controller.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer
import java.math.BigDecimal
import java.time.LocalDateTime

data class OrderCreateResponse(
    val orderNumber: String,
    val orderStatus: String,
    val orderDate: String,
    val totalAmount: BigDecimal,
    val products: List<ProductSummary>,
) {
    data class ProductSummary(
        val id: Long,
        val name: String,
        val quantity: Int,
        val price: BigDecimal
    )
}