package com.commerce.service.order.applicaton.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse

interface OrderUseCase {
    fun getOrder(request: OrderListRequest, member: Member) : OrderListResponse
    fun getOrderDetail(id: String): OrderDetailResponse
}