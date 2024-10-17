package com.commerce.service.order.application.usecase.dto

import com.commerce.common.model.util.PaginationInfo

class OrderListDto (
    val products: List<OrderSummaryDto>,
    val paginationInfo: PaginationInfo
)