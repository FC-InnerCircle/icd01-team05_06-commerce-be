package com.commerce.service.order.controller.response

import com.commerce.common.model.util.PaginationInfo
import com.commerce.service.order.application.usecase.dto.OrderListDto

data class OrderListResponse(
    val products: List<OrderSummary>,
    val paginationInfo: PaginationInfo,
) {
    companion object {
        fun from(dto: OrderListDto): OrderListResponse {
            return OrderListResponse(
                products = dto.products.map { OrderSummary.from(it) },
                paginationInfo = dto.paginationInfo
            )
        }
    }
}