package com.commerce.service.order.applicaton.service

import com.commerce.common.model.order.OrderRepository
import com.commerce.service.order.applicaton.usecase.OrderUseCase
import com.commerce.service.order.applicaton.usecase.exception.InvalidInputException
import com.commerce.service.order.controller.request.OrderListRequest
import com.commerce.service.order.controller.response.OrderListResponse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService (
    private val orderRepository: OrderRepository
) : OrderUseCase {
    override fun getOrder(request: OrderListRequest) : OrderListResponse {
        if (request.page < 0) {
            throw InvalidInputException("Page number cannot be negative")
        }

        val (startDate, endDate) = getDateRange(request.dateRange, request.startDate, request.endDate)
        val sort = when (request.sortBy) {
            OrderListRequest.SortOption.RECENT -> Sort.by(Sort.Direction.DESC, "orderDate")
            OrderListRequest.SortOption.ORDER_STATUS -> Sort.by("orderStatus", "orderDate")
            OrderListRequest.SortOption.ALL -> Sort.unsorted()
        }
        val pageable = PageRequest.of(request.page, request.size, sort)

//        val orders = if (request.status != null) {
//            orderRepository.findByOrderDateBetweenAndOrderStatus(startDate, endDate, request.status, pageable)
//        } else {
//            orderRepository.findByOrderDateBetween(startDate, endDate, pageable)
//        }

//        return OrderListResponse(
//            orders = orders.content.map { it.toOrderSummary() },
//            totalElements = orders.totalElements,
//            totalPages = orders.totalPages
//        )

        return OrderListResponse(
            orders = emptyList(),
            totalElements = 0,
            totalPages = 0
        )
    }

    override fun getOrderDetail() {
        return
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
}