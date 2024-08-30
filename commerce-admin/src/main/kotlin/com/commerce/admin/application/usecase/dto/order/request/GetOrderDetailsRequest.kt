package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest

data class GetOrderDetailsRequest(
    val orderId: String,
) : CommonRequest {
    override fun validate() {
        require(orderId.isNotBlank()) { "Order ID cannot be blank" }
    }
}
