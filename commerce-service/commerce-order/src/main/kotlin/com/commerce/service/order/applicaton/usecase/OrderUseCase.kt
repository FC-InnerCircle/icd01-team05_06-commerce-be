package com.commerce.service.order.applicaton.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse

interface OrderUseCase {
    // 주문 생성
    fun order()
    // 주문 목록 조회
    fun getOrder(request: OrderListRequest, member: Member) : OrderListResponse
    // 주문 상세 조회
    fun getOrderDetail(id: String): OrderDetailResponse
    fun createOrder(request: OrderCreateRequest): OrderCreateResponse
}