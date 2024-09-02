package com.commerce.service.product.controller.request

import com.commerce.service.product.application.usecase.query.SelectQuery
import org.springframework.data.domain.PageRequest

data class ProductQueryRequest(
    val productCategoryId: Long,
    val page: Int = 0,
    val size: Int = 20,
) {
    fun toQuery(): SelectQuery {
        return SelectQuery(
            categoryId = productCategoryId,
            page = page,
            size = size,
        )
    }
}