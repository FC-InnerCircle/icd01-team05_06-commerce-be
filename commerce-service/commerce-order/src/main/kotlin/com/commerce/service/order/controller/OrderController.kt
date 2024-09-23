package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse
import jakarta.validation.Valid
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
        request.validate()
        val response = orderUseCase.getOrder(request, member)
        return ResponseEntity.ok(CommonResponse.ok(data = response))
    }

    // 주문 상세 조회 API
    @GetMapping("/{orderId}")
    fun getOrderDetail(@PathVariable orderId: String): ResponseEntity<CommonResponse<OrderDetailResponse>> {
        val response = orderUseCase.getOrderDetail(orderId)
        return ResponseEntity.ok(CommonResponse.ok(data = response))
    }

    // 주문 생성 API
    @PostMapping("/create")
    fun createOrder(@Valid @RequestBody request: OrderDetail): ResponseEntity<CommonResponse<OrderDetailResponse>> {
//        return ResponseEntity.ok(CommonResponse(success = true, data = OrderDetailResponse(order = request)))
        val response = null;
        return ResponseEntity.ok(CommonResponse.ok(data = response))
    }

}