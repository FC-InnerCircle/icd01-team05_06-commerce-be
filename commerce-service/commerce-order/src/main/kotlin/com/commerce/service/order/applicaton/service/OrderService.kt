package com.commerce.service.order.applicaton.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.applicaton.usecase.converter.toOrder
import com.commerce.service.order.applicaton.usecase.converter.toOrderSummary
import com.commerce.service.order.applicaton.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class OrderService (
    private val ordersRepository: OrdersRepository
) : OrderUseCase {
    override fun getOrder(request: OrderListRequest, member: Member) : OrderListResponse {
        if (request.page < 0) {
            throw InvalidInputException("Page number should be 0 or greater")
        }

        val memberId = member.id

        val (orderDate, endDate) = getDateRange(request.dateRange, request.orderDate, request.endDate)

        val status = request.status?.let { OrderStatus.valueOf(it.name) }

        val ordersPage = ordersRepository.findByMemberIdAndOrderDateBetween(
            memberId, orderDate, endDate, status, request.page, request.size, request.sortBy
        )

        val orders = ordersPage.data.map { it.toOrder() }

        return OrderListResponse(
            products = orders.map { it.toOrderSummary() },
            totalElements = ordersPage.pagination.totalCount,
            totalPages = ordersPage.pagination.totalPage
        )
    }

    override fun getOrderDetail(id: String): OrderDetailResponse {
//        val order = orderRepository.findById(id).orElseThrow { OrderNotFoundException(id) }
//        return OrderDetailResponse(
//            order = order.toOrderDetail(),
//            items = order.items.map { it.toOrderItem() },
//            statusHistory = order.statusHistory.map { it.toStatusHistoryItem() }
//        )
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

    private fun getDateRange(range: OrderListRequest.DateRange, startDate: LocalDate?, endDate: LocalDate?): Pair<LocalDateTime, LocalDateTime> {
        val now = LocalDateTime.now()
        return when (range) {
            OrderListRequest.DateRange.LAST_WEEK -> now.minusWeeks(1) to now
            OrderListRequest.DateRange.LAST_MONTH -> now.minusMonths(1) to now
            OrderListRequest.DateRange.LAST_3_MONTHS -> now.minusMonths(3) to now
            OrderListRequest.DateRange.LAST_6_MONTHS -> now.minusMonths(6) to now
            OrderListRequest.DateRange.CUSTOM -> {
                if (startDate == null || endDate == null) {
                    throw InvalidInputException("Start date and end date are required for custom date range")
                }
                val startDateTime = startDate.atStartOfDay()
                val endDateTime = endDate.atTime(23, 59, 59)

                startDateTime to endDateTime
            }
        }
    }
//    private fun Order.toOrderDetail() = OrderDetail(
//        id = this.id,
//        orderNumber = this.id,
//        orderDate = this.orderDate,
//        status = this.status,
//        totalAmount = this.totalAmount,
//        customerName = this.customerName,
//        shippingAddress = this.streetAddress + " " + this.detailAddress,
//        paymentMethod = "Credit Card" // Example, replace with actual field
//    )
}