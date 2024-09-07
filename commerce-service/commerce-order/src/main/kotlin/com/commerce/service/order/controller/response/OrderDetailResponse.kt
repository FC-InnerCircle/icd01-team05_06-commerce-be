package com.commerce.service.order.controller.response

import com.commerce.common.model.orderProduct.OrderProduct

data class OrderDetailResponse(
    val order: OrderDetail,
    val items: List<OrderProduct>,
    val statusHistory: List<StatusHistoryItem>
)
