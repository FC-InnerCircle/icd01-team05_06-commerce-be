package com.commerce.service.order.applicaton.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.applicaton.usecase.component.PaymentHandler
import com.commerce.service.order.applicaton.usecase.component.ProductHandler
import com.commerce.service.order.applicaton.usecase.component.ShippingHandler
import com.commerce.service.order.applicaton.usecase.converter.toOrder
import com.commerce.service.order.applicaton.usecase.converter.toOrderSummary
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService (
    private val ordersRepository: OrdersRepository,

    // TODO: 주문 생성, 배송, 결제 로직 추가
    private val productHandler: ProductHandler,
    private val shippingHandler: ShippingHandler,
    private val paymentHandler: PaymentHandler
) : OrderUseCase {

    /**
     * 주문 생성 로직
     * 1. 상품 관련 처리 (ProductHandler)
     * 2. 결제 처리 (PaymentHandler)
     * 3. 상품 재고 업데이트 (ProductHandler)
     * 4. 배송 처리 (ShippingHandler)
     * 5. 주문 완료 처리 (ProductHandler)
     */
    override fun order() {
        TODO("Not yet implemented")
    }

    override fun getOrder(request: OrderListRequest, member: Member) : OrderListResponse {
        val (orderDate, endDate) = request.dateRange.getStartToEnd(request.orderStartDate, request.orderEndDate)

        val ordersPage = ordersRepository.findByMemberIdAndOrderDateBetween(
            member.id, orderDate, endDate, request.status, request.page, request.size, request.sortBy
        )

        return OrderListResponse(
            products = ordersPage.data.map { it.toOrder().toOrderSummary() },
            paginationInfo = ordersPage.pagination
        )
    }

    // TODO: 주문 번호로 주문 상세 정보 조회
    override fun getOrderDetail(id: String): OrderDetailResponse {
        return OrderDetailResponse(
            order = OrderDetail(
                id = "1",
                orderNumber = "1",
                orderDate = LocalDateTime.now(),
                status = "Order Placed",
                totalAmount = 100.0,
                customerName = "John Doe",
                shippingAddress = "123 Main St, Springfield, IL 62701",
                paymentMethod = "Credit Card"
            ),

            items = listOf(),
            statusHistory = listOf()
        )
    }
}