package com.commerce.service.product.application.usecase.query

import com.commerce.common.model.product.HomeProductType

data class SelectQuery(
    val categoryId: Long?,
    val searchWord: String?,
    val homeProductType: HomeProductType?,
    val page: Int,
    val size: Int,
)