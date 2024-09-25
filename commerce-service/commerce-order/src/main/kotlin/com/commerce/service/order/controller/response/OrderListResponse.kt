package com.commerce.service.order.controller.response

import com.commerce.common.model.util.PaginationInfo

data class OrderListResponse(
    val products: List<OrderSummary>,
    val paginationInfo: PaginationInfo,
)