package com.commerce.service.product.application.usecase.query

data class BeforeOrderQuery(
    val products: List<IdAndQuantity>
) {
    data class IdAndQuantity(
        val productId: Long,
        val quantity: Int
    )
}