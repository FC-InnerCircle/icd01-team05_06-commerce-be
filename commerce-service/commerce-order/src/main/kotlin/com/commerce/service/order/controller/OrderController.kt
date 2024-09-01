package com.commerce.service.order.controller

import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.common.responese.CommonResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.request.OrderListRequest
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(ApiPaths.ORDERS)
class OrderController (
    private val orderUseCase: OrderUseCase
) {
    @GetMapping
    fun getOrder(@Valid @RequestBody request: OrderListRequest): ResponseEntity<CommonResponse<OrderListResponse>> {
//        return orderUseCase.getOrder()
        return ResponseEntity.ok(CommonResponse(success = true, data = null))
    }

    @GetMapping("/{orderId}")
    fun getOrderDetail() {
        return orderUseCase.getOrderDetail()
    }
}