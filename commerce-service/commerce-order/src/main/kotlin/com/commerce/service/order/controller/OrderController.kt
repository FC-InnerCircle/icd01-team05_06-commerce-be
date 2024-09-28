package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.config.ApiPaths
import com.commerce.service.order.controller.request.OrderCreateRequest
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderCreateResponse
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.time.LocalDateTime

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
    @PostMapping
    fun createOrder(@Valid @RequestBody request: OrderCreateRequest): ResponseEntity<CommonResponse<OrderCreateResponse>> {
//        return ResponseEntity.ok(CommonResponse(success = true, data = OrderDetailResponse(order = request)))

        // 임시 응답값
        val response = OrderCreateResponse(
            orderId = 12345L,
            orderDate = LocalDateTime.now(),
            totalAmount = 50000,
            status = "PENDING",
            products = listOf(
                OrderCreateResponse.ProductInfo(
                    bookId = 1L,
                    title = "Kotlin in Action",
                    quantity = 2,
                    price = 20000
                ),
                OrderCreateResponse.ProductInfo(
                    bookId = 2L,
                    title = "Spring Boot in Practice",
                    quantity = 1,
                    price = 10000
                )
            ),
            deliveryInfo = OrderCreateResponse.DeliveryInfo(
                name = request.deliveryInfo.recipient,
                phoneNumber = "010-1234-5678",  // 실제로는 요청에서 받아와야 합니다
                streetAddress = request.deliveryInfo.streetAddress,
                detailAddress = request.deliveryInfo.detailAddress
            ),
            paymentInfo = OrderCreateResponse.PaymentInfo(
                method = request.paymentInfo.method,
                lastFourDigits = request.paymentInfo.cardNumber.takeLast(4)
            )
        )

        return ResponseEntity.ok(CommonResponse.ok(data = response))
    }

}