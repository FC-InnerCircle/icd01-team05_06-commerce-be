package com.commerce.service.product.controller.request

import com.commerce.common.model.product.HomeProductType
import com.commerce.service.product.application.usecase.query.SelectQuery
import java.math.BigDecimal

data class ProductQueryRequest(
    val productCategoryId: Long?,
    val searchWord: String?,
    val homeProductType: HomeProductType?,
    val minPrice: BigDecimal?,
    val maxPrice: BigDecimal?,
    override val page: Int,
    override val size: Int,
) : BasePageRequest {
    fun toQuery(): SelectQuery {
        return SelectQuery(
            categoryId = productCategoryId,
            searchWord = searchWord,
            homeProductType = homeProductType,
            minPrice = minPrice,
            maxPrice = maxPrice,
            page = this.page,
            size = this.size,
        )
    }
}