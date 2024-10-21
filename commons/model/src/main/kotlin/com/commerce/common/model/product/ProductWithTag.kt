package com.commerce.common.model.product

data class ProductWithTag(
    val product: Product,
    val tags: List<String>
)
