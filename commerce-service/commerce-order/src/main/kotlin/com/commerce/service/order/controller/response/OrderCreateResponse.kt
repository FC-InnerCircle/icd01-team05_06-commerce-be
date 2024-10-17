package com.commerce.service.order.controller.response

import com.commerce.service.order.application.usecase.dto.CreateOrderDto
import com.commerce.service.order.application.usecase.dto.ProductSummaryDto
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
    ) {
        companion object {
            fun from(it: ProductSummaryDto): ProductSummary {
                return ProductSummary(
                    id = it.id,
                    name = it.name,
                    quantity = it.quantity,
                    price = it.price,
                    discountedPrice = it.discountedPrice
                )
            }
        }
    }

    companion object {
        fun from(dto: CreateOrderDto): OrderCreateResponse {
            return OrderCreateResponse(
                id = dto.id,
                orderNumber = dto.orderNumber,
                orderStatus = dto.orderStatus,
                orderDate = dto.orderDate,
                products = dto.products.map { ProductSummary.from(it) }
            )
        }
    }
}