package com.commerce.service.order.applicaton.usecase

import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderListResponse

interface OrderUseCase {
    fun getOrder(request: OrderListRequest) : OrderListResponse
    fun getOrderDetail()
}