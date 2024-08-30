@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.commerce.admin.controller
import com.commerce.admin.application.usecase.dto.ApiResponse
import com.commerce.admin.application.usecase.dto.order.request.*
import com.commerce.admin.application.usecase.dto.order.response.*
import com.commerce.admin.config.ApiPaths
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping(ApiPaths.ORDERS)
class OrderController : OrderSwaggerDocumentation {
    @GetMapping
    override fun getOrders(
        @ModelAttribute request: GetOrdersRequest,
    ): ResponseEntity<ApiResponse<OrderListResponse>> {
        request.validate()
//        val orders = orderService.getOrders(request.page, request.limit, request.sort, request.status)
//        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"))

        val dummyOrders =
            listOf(
                OrderSummary("ord_1", "John Doe", 100.0, "PROCESSING", LocalDateTime.now()),
                OrderSummary("ord_2", "Jane Smith", 150.0, "SHIPPED", LocalDateTime.now().minusDays(1)),
            )
        val response = OrderListResponse(dummyOrders, 2, request.page, request.limit)
        return ResponseEntity.ok(ApiResponse.success(response, "Orders retrieved successfully"))
    }

    @GetMapping("/{orderId}")
    override fun getOrderDetails(
        @PathVariable orderId: String,
    ): ResponseEntity<ApiResponse<OrderDetails>> {
        val request = GetOrderDetailsRequest(orderId)
        request.validate()
//        val orderDetails = orderService.getOrderDetails(request.orderId)
//        return ResponseEntity.ok(ApiResponse.success(orderDetails, "Order details retrieved successfully"))

        return ResponseEntity.ok(ApiResponse.success(null, "Order details retrieved successfully"))
    }

    @PostMapping("/{orderId}/update-status")
    override fun updateOrderStatus(
        @PathVariable orderId: String,
        @RequestBody request: UpdateOrderStatusRequest,
    ): ResponseEntity<ApiResponse<UpdateStatusResponse>> {
        request.validate()
//        val response = orderService.updateOrderStatus(orderId, request.status)
//        return ResponseEntity.ok(ApiResponse.success(response, "Order status updated successfully"))

        return ResponseEntity.ok(ApiResponse.success(null, "Order status updated successfully"))
    }

    @PostMapping("/{orderId}/cancel")
    override fun cancelOrder(
        @PathVariable orderId: String,
        @RequestBody request: CancelOrderRequest,
    ): ResponseEntity<ApiResponse<CancelOrderResponse>> {
        request.validate()
//        val response = orderService.cancelOrder(orderId, request.reason)
//        return ResponseEntity.ok(ApiResponse.success(response, "Order cancelled successfully"))

        return ResponseEntity.ok(ApiResponse.success(null, "Order cancelled successfully"))
    }

    @GetMapping("/search")
    override fun searchOrders(
        @ModelAttribute request: SearchOrdersRequest,
    ): ResponseEntity<ApiResponse<OrderListResponse>> {
        request.validate()
//        val searchResult =
//            orderService.searchOrders(
//                request.query,
//                request.status,
//                request.startDate,
//                request.endDate,
//                request.minAmount,
//                request.maxAmount,
//                request.page,
//                request.limit,
//            )
//        return ResponseEntity.ok(ApiResponse.success(searchResult, "Orders searched successfully"))

        return ResponseEntity.ok(ApiResponse.success(null, "Orders searched successfully"))
    }

    @GetMapping("/statistics")
    override fun getOrderStatistics(
        @ModelAttribute request: GetOrderStatisticsRequest,
    ): ResponseEntity<ApiResponse<OrderStatistics>> {
        request.validate()
//        val statistics = orderService.getOrderStatistics(request.startDate, request.endDate)
//        return ResponseEntity.ok(ApiResponse.success(statistics, "Order statistics retrieved successfully"))

        return ResponseEntity.ok(ApiResponse.success(null, "Order statistics retrieved successfully"))
    }

    @PostMapping("/export")
    override fun exportOrders(
        @RequestBody request: ExportOrdersRequest,
    ): ResponseEntity<ApiResponse<ExportOrdersResponse>> {
        request.validate()
//        val response = orderService.exportOrders(request.startDate, request.endDate, request.format)
//        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success(response, "Order export initiated successfully"))

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(ApiResponse.success(null, "Order export initiated successfully"))
    }
}
