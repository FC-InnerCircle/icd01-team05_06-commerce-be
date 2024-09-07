package com.commerce.service.order.applicaton.service

import com.commerce.common.model.orders.Orders
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orders.OrderStatus
import com.commerce.service.order.applicaton.usecase.OrderUseCase
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
    override fun getOrder(request: OrderListRequest) : OrderListResponse {
        if (request.page < 0) {
            throw InvalidInputException("Page number cannot be negative")
        }

        val (startDate, endDate) = getDateRange(request.dateRange, request.startDate, request.endDate)
        val sort = when (request.sortBy) {
            OrderListRequest.SortOption.RECENT -> Sort.by(Sort.Direction.DESC, "createdAt")
            OrderListRequest.SortOption.ORDER_STATUS -> Sort.by("status", "createdAt")
            OrderListRequest.SortOption.ALL -> Sort.unsorted()
        }
        val pageable = PageRequest.of(request.page, request.size, sort)

        val ordersPage = if (request.status != null) {
            val status = OrderStatus.valueOf(request.status)
            ordersRepository.findByCreatedAtBetweenAndStatus(startDate, endDate, status, pageable)
        } else {
            ordersRepository.findByCreatedAtBetween(startDate, endDate, pageable)
        }

        val orders = ordersPage.content.map { toOrder(it) }

        return OrderListResponse(
            products = orders.map { toOrderSummary(it) },
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

    private fun toOrderSummary(orders: Orders): OrderSummary {
        return OrderSummary(
            id = orders.id.toString(),
            orderNumber = orders.orderDate.toString() + "-" + orders.memberId, // 주문번호 = 주문 일자 + 주문자 ID (임시)
            content = orders.content,
            orderDate = orders.orderDate.toString(),
            status = orders.status.toString(),
            pricie = orders.price.toDouble(),
            discoutedPrice = orders.discountedPrice.toDouble(),
            memberName = "Customer-${orders.memberId}", // 임시 처리
            recipient = orders.recipient
        )
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

    private fun toOrder(orders: Orders): Orders {
        return Orders(
            id = orders.id,
            memberId = orders.memberId,
            streetAddress = orders.streetAddress,
            detailAddress = orders.detailAddress,
            postalCode = orders.postalCode,
            orderNumber = orders.orderNumber,
            paymentMethod = orders.paymentMethod,
            recipient = orders.recipient,
            content = orders.content,
            discountedPrice = orders.discountedPrice,
            price = orders.price,
            status = OrderStatus.valueOf(orders.status.name),
            orderDate = orders.orderDate,
            createdAt = orders.createdAt,
            updatedAt = orders.updatedAt,
            orderProducts = orders.orderProducts.map { toOrderProducts(it) }
        )
    }


    private fun toOrderProducts(orderProuct: OrderProduct): OrderProduct {
        return OrderProduct(
            id = orderProuct.id,
            orderId = orderProuct.orderId,
            productId = orderProuct.productId,
            quantity = orderProuct.quantity,
            price = orderProuct.price,
            discountedPrice = orderProuct.discountedPrice,
            createdAt = orderProuct.createdAt,
            updatedAt = orderProuct.updatedAt
        )
    }
}