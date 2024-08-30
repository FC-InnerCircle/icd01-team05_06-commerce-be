package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest

data class CancelOrderRequest(
    val orderId: String,
    val reason: String,
) : CommonRequest {
    override fun validate() {
        require(orderId.isNotBlank()) { "Order ID cannot be blank" }
        require(reason.isNotBlank()) { "Reason cannot be blank" }
    }
}
