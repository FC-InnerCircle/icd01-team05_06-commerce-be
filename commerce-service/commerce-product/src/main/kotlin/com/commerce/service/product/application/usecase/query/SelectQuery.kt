package com.commerce.service.product.application.usecase.query

data class SelectQuery(
    val categoryId: Long?,
    val searchWord: String?,
    val page: Int,
    val size: Int,
)