package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.OrderSortOption
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.Orders
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.util.PaginationModel
import com.commerce.common.persistence.util.toPaginationModel
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Repository
class OrdersRepositoryImpl(
    private val ordersJpaRepository: OrdersJpaRepository,
) : OrdersRepository {

    override fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDate,
        endDate: LocalDate,
        status: OrderStatus?,
        page: Int,
        size: Int,
        sortOption: OrderSortOption
    ): PaginationModel<Orders> {
        val sort = when (sortOption) {
            OrderSortOption.RECENT -> Sort.by(Sort.Direction.DESC, "orderDate")
            OrderSortOption.ORDER_STATUS -> Sort.by("status", "orderDate")
            OrderSortOption.ALL -> Sort.unsorted()
        }
        val pageable = PageRequest.of(page, size, sort)

        val pageResult = if (status != null) {
            ordersJpaRepository.findByMemberIdAndOrderDateBetweenAndStatus(
                memberId, orderDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), status, pageable
            )
        } else {
            ordersJpaRepository.findByMemberIdAndOrderDateBetween(
                memberId, orderDate.atStartOfDay(), LocalDateTime.of(endDate, LocalTime.MAX), pageable
            )
        }

        return pageResult.toPaginationModel { it.toOrder() }
    }

    override fun save(orders: Orders): Orders {
        return ordersJpaRepository.save(orders.toJpaEntity()).toOrder()
    }
}