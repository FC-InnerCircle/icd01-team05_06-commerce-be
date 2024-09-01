package com.commerce.service.order.controller

import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.common.responese.CommonResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(ApiPaths.ORDERS)
class OrderController (
    private val orderUseCase: OrderUseCase
) {
    @PostMapping
    fun getOrder(@Valid @RequestBody request: OrderListRequest): ResponseEntity<CommonResponse<OrderListResponse>> {
        request.validate()
        val response = orderUseCase.getOrder(request)
        return ResponseEntity.ok(CommonResponse(success = true, data = response))
    }

    @GetMapping("/{orderId}")
    fun getOrderDetail(): ResponseEntity<CommonResponse<OrderDetailResponse>> {
//        return orderUseCase.getOrderDetail()
        return ResponseEntity.ok(CommonResponse(success = true, data = null))
    }
}