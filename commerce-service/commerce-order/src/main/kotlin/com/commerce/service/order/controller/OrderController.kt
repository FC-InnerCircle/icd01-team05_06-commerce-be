package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderNumber
import com.commerce.common.response.CommonResponse
import com.commerce.service.order.application.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.response.OrderResultResponse
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

// 미개발 API는 Mock Data를 리턴하도록 구현
@RestController
@RequestMapping(ApiPaths.ORDERS)
class OrderController(
    private val orderUseCase: OrderUseCase
) {
    // 주문 조회 API
    @GetMapping
    fun getOrder(@Validated request: OrderListRequest,  @AuthenticationPrincipal member: Member): ResponseEntity<CommonResponse<OrderListResponse>> {
        val response = orderUseCase.getOrder(request.toCommand(member))
        return ResponseEntity.ok(CommonResponse.ok(data = OrderListResponse.from(response)))
    }

    // 주문 상세 조회 API
    @GetMapping("/{orderNumber}")
    fun getOrderDetail(@AuthenticationPrincipal member: Member, @PathVariable orderNumber: String): ResponseEntity<CommonResponse<OrderResultResponse>> {
        val ordersResult = orderUseCase.getOrderResult(member, OrderNumber(orderNumber))
        return ResponseEntity.ok(CommonResponse.ok(data = OrderResultResponse.from(ordersResult)))
    }

    // 주문 생성 API
    @PostMapping
    fun createOrder(@Validated @RequestBody request: OrderCreateRequest, @AuthenticationPrincipal member: Member): ResponseEntity<CommonResponse<OrderCreateResponse>> {
        val response = orderUseCase.order(request.toCommand(member))
        return ResponseEntity.ok(CommonResponse.ok(data = OrderCreateResponse.from(response)))
    }
}