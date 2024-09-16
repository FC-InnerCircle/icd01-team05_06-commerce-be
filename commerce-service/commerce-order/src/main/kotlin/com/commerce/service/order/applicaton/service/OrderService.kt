package com.commerce.service.order.applicaton.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.orders.Orders
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orders.OrderStatus
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.applicaton.usecase.converter.toOrder
import com.commerce.service.order.applicaton.usecase.converter.toOrderSummary
import com.commerce.service.order.applicaton.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderDetail
import com.commerce.service.order.controller.response.OrderDetailResponse
import com.commerce.service.order.controller.response.OrderListResponse
import com.commerce.service.order.controller.response.OrderSummary
import org.springframework.data.domain.PageRequest

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService (
    private val ordersRepository: OrdersRepository
) : OrderUseCase {
    override fun getOrder(request: OrderListRequest, member: Member) : OrderListResponse {
        if (request.page < 0) {
            throw InvalidInputException("Page number cannot be negative")
        }

        val memberId = member.id

        val (startDate, endDate) = getDateRange(request.dateRange, request.startDate, request.endDate)
        val sort = when (request.sortBy) {
            OrderListRequest.SortOption.RECENT -> Sort.by(Sort.Direction.DESC, "createdAt")
            OrderListRequest.SortOption.ORDER_STATUS -> Sort.by("status", "createdAt")
            OrderListRequest.SortOption.ALL -> Sort.unsorted()
        }
        val pageable = PageRequest.of(request.page, request.size, sort)

        val ordersPage = if (request.status != null) {
            val status = OrderStatus.valueOf(request.status.name)
            // ordersRepository.findByCreatedAtBetweenAndStatus(startDate, endDate, status, pageable)
            ordersRepository.findByMemberIdAndCreatedAtBetweenAndStatus(memberId, startDate, endDate, status, pageable)
        } else {
            // ordersRepository.findByCreatedAtBetween(startDate, endDate, pageable)
            ordersRepository.findByMemberIdAndCreatedAtBetween(memberId, startDate, endDate, pageable)
        }

        val orders = ordersPage.content.map { it.toOrder() }

        return OrderListResponse(
            products = orders.map { it.toOrderSummary() },
            totalElements = ordersPage.totalElements,
            totalPages = ordersPage.totalPages
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

    private fun getDateRange(range: OrderListRequest.DateRange, startDate: LocalDateTime?, endDate: LocalDateTime?): Pair<LocalDateTime, LocalDateTime> {
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
                startDate to endDate
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