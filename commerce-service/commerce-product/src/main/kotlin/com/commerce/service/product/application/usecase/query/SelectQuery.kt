package com.commerce.service.product.application.usecase.query

import com.commerce.common.model.product.HomeProductType
import java.math.BigDecimal

data class SelectQuery(
    val categoryId: Long?,
    val searchWord: String?,
    val homeProductType: HomeProductType?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?,
    val page: Int,
    val size: Int,
)