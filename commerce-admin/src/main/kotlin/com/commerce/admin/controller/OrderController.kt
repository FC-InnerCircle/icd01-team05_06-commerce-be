package com.commerce.admin.controller

import com.commerce.admin.application.usecase.dto.OrderDto
import com.commerce.admin.application.usecase.dto.OrderListResponse
import com.commerce.common.model.order.Order
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.Objects

@RestController
@RequestMapping("/admin/v1/orders")
class OrderController {
    @GetMapping
    fun getOrders(@RequestParam(defaultValue = "1") page: Int,
                  @RequestParam(defaultValue = "20") limit: Int,
                  @RequestParam(required = false) sort: String?,
                  @RequestParam(required = false) status: String?): OrderListResponse {
        // 실제 로직 대신 더미 데이터 반환
        return OrderListResponse(
            orders = listOf(
                OrderDto("ord_1", "John Doe", 100.0, "PROCESSING", "2023-01-01T00:00:00Z", 2)
            ),
            total = 1,
            page = page,
            limit = limit,
            totalPages = 1
        )
    }
}