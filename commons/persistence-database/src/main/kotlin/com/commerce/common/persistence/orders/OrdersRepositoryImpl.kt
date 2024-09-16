package com.commerce.common.persistence.orders

import com.commerce.common.model.orders.Orders
import com.commerce.common.model.orders.OrdersRepository
import com.commerce.common.model.orderProduct.OrderProduct
import com.commerce.common.model.orders.OrderStatus
import com.commerce.common.model.orders.toJpaStatus
import com.commerce.common.persistence.orderProduct.OrderProductJpaEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import org.springframework.data.domain.Pageable

@Repository
class OrdersRepositoryImpl (
    private val ordersJpaRepository: OrdersJpaRepository
) : OrdersRepository {

    override fun findByMemberIdAndCreatedAtBetween(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        pageable: Pageable
    ): Page<Orders> {
        val orderJpaEntities = ordersJpaRepository.findByMemberIdAndCreatedAtBetween(memberId, startDate, endDate, pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }

    override fun findByMemberIdAndCreatedAtBetweenAndStatus(
        memberId: Long,
        startDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus,
        pageable: Pageable
    ): Page<Orders> {
        val orderJpaEntities = ordersJpaRepository.findByMemberIdAndCreatedAtBetweenAndStatus(memberId, startDate, endDate, status.toJpaStatus(), pageable)
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }
}