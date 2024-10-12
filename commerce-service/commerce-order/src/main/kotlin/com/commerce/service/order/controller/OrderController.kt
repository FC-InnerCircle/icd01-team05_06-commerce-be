package com.commerce.service.order.controller

import com.commerce.common.model.member.Member
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
    fun getOrder(
        @Validated request: OrderListRequest,
        @AuthenticationPrincipal member: Member
    ): ResponseEntity<CommonResponse<OrderListResponse>> {
        val response = orderUseCase.getOrder(request.toCommand(member))
        return ResponseEntity.ok(CommonResponse.ok(data = response))
    }

    // 주문 상세 조회 API
    @GetMapping("/{orderId}")
    fun getOrderDetail(@AuthenticationPrincipal member: Member, @PathVariable orderId: Long): ResponseEntity<CommonResponse<OrderResultResponse>> {
        val ordersResult = orderUseCase.getOrderResult(member, orderId)
        return ResponseEntity.ok(CommonResponse.ok(data = OrderResultResponse.from(ordersResult)))
    }

    // 주문 생성 API
    @PostMapping
    fun createOrder(
        @Validated @RequestBody request: OrderCreateRequest,
        @AuthenticationPrincipal member: Member
    ): ResponseEntity<CommonResponse<OrderCreateResponse>> {
        val list = mutableListOf<OrderCreateResponse>()
        for (i in 1..100) {
            val itemRequest = OrderCreateRequest(
                products = listOf(
                    OrderCreateRequest.ProductInfo(
                        id = (i % 100).toLong(),
                        quantity = 1
                    ),
                    OrderCreateRequest.ProductInfo(
                        id = (i % 100 + 1).toLong(),
                        quantity = 2
                    )
                ),
                ordererInfo = OrderCreateRequest.OrdererInfo(
                    name = "주문자$i",
                    phoneNumber = "01012345${i.toString().padStart(3, '0')}",
                    email = "test$i@example.com"
                ), deliveryInfo = OrderCreateRequest.DeliveryInfo(
                    recipient = "수령인$i",
                    phoneNumber = "01054321${i.toString().padStart(3, '0')}",
                    streetAddress = "서울 종로구 종로3가 $i",
                    detailAddress = "종로아파트 ${i}호",
                    postalCode = "12${i.toString().padStart(3, '0')}",
                    memo = if (i % 2 == 0) "배송메모$i" else null,
                ), paymentInfo = OrderCreateRequest.PaymentInfo(
                    method = "CREDIT_CARD",
                    depositorName = "예금주$i"
                ), agreementInfo = OrderCreateRequest.AgreementInfo(
                    termsOfService = true,
                    privacyPolicy = true,
                    ageVerification = true
                )
            )
            list.add(orderUseCase.order(itemRequest.toCommand(member)))
        }
        return ResponseEntity.ok(CommonResponse.ok(data = list.first()))
    }
}