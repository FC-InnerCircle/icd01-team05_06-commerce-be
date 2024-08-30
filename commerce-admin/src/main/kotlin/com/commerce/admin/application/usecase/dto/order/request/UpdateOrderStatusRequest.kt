package com.commerce.admin.application.usecase.dto.order.request

import com.commerce.admin.application.usecase.dto.CommonRequest

data class UpdateOrderStatusRequest(
    val orderId: String,
    val status: String,
) : CommonRequest {
    override fun validate() {
        require(orderId.isNotBlank()) { "Order ID cannot be blank" }
        require(status.isNotBlank()) { "Status cannot be blank" }
    }
}
