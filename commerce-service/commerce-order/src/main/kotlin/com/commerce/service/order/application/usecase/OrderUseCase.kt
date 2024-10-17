package com.commerce.service.order.application.usecase

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.model.orders.OrdersResult
import com.commerce.service.order.application.usecase.command.CreateOrderCommand
import com.commerce.service.order.application.usecase.command.OrderListCommand
import com.commerce.service.order.application.usecase.dto.CreateOrderDto
import com.commerce.service.order.application.usecase.dto.OrderListDto
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderListResponse

interface OrderUseCase {
    // 주문 생성
    fun order(command: CreateOrderCommand) : CreateOrderDto
    // 주문 목록 조회
    fun getOrder(command: OrderListCommand) : OrderListDto
    // 주문 상세 조회
    fun getOrderResult(member: Member, orderNumber: OrderNumber): OrdersResult
}