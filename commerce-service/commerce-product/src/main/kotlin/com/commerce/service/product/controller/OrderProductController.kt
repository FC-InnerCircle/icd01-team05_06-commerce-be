package com.commerce.service.product.controller

import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.OrderProductUseCase
import com.commerce.service.product.controller.request.BeforeOrderRequest
import com.commerce.service.product.controller.response.BeforeOrderResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/v1/products/order")
class OrderProductController(
    private val orderProductUseCase: OrderProductUseCase,
) {

    @PostMapping("/before")
    fun beforeOrder(@RequestBody request: BeforeOrderRequest): CommonResponse<BeforeOrderResponse> {
        val products = orderProductUseCase.getBeforeOrderProducts(request.toQuery())
        return CommonResponse.ok(BeforeOrderResponse(products))
    }
}