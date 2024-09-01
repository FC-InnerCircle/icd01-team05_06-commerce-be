package com.commerce.service.order.controller.response

import com.commerce.common.model.orderItem.OrderItem

data class OrderDetailResponse(
    val order: OrderDetail,
    val items: List<OrderItem>,
    val statusHistory: List<StatusHistoryItem>
)
