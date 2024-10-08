package com.commerce.service.order.application.usecase

import com.commerce.common.model.member.Member
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.command.OrderListCommand
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse

interface OrderUseCase {
    // 주문 생성
    fun order(command: CreateOrderCommand) : OrderCreateResponse
    // 주문 목록 조회
    fun getOrder(command: OrderListCommand) : OrderListResponse
    // 주문 상세 조회
    fun getOrderDetail(id: String): OrderDetailResponse
}