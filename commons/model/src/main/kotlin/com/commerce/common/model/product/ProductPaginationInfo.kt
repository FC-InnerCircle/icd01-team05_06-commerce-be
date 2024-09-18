package com.commerce.common.model.product

data class ProductPaginationInfo(
    val data: List<Product>,
    val pagination: PaginationInfo
)