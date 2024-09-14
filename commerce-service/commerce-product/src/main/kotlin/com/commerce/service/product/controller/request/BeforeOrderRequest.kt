package com.commerce.service.product.controller.request

import com.commerce.service.product.application.usecase.query.BeforeOrderQuery

data class BeforeOrderRequest(
    val products: List<IdAndQuantity>
) {
    data class IdAndQuantity(
        val productId: Long,
        val quantity: Int
    )

    fun toQuery() = BeforeOrderQuery(
        products.map {
            BeforeOrderQuery.IdAndQuantity(
                it.productId,
                it.quantity
            )
        }
    )
}