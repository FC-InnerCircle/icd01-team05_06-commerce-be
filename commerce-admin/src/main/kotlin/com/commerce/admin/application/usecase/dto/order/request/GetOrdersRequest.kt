package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest

data class GetOrdersRequest(
    val page: Int = 1,
    val limit: Int = 20,
    val sort: String? = null,
    val status: String? = null,
) : CommonRequest {
    override fun validate() {
        require(page > 0) { "Page must be greater than 0" }
        require(limit > 0) { "Limit must be greater than 0" }
    }
}
