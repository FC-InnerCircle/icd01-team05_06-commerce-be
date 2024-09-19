package com.commerce.service.product.controller.request

import com.commerce.service.product.application.usecase.query.SelectQuery

data class ProductQueryRequest(
    val productCategoryId: Long?,
    val searchWord: String?,
    override val page: Int,
    override val size: Int,
    ) : BasePageRequest{
    fun toQuery(): SelectQuery {
        return SelectQuery(
            categoryId = productCategoryId,
            searchWord = searchWord,
            page = this.page,
            size = this.size,
        )
    }
}