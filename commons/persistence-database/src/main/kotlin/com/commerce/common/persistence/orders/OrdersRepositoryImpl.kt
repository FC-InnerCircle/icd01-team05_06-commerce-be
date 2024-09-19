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

    override fun findByMemberIdAndOrderDateBetween(
        memberId: Long,
        orderDate: LocalDateTime,
        endDate: LocalDateTime,
        status: OrderStatus?,
        pageable: Pageable
    ): Page<Orders> {
        val orderJpaEntities = if (status != null) {
            ordersJpaRepository.findByMemberIdAndOrderDateBetweenAndStatus(
                memberId, orderDate, endDate, status.toJpaStatus(), pageable
            )
        } else {
            ordersJpaRepository.findByMemberIdAndOrderDateBetween(
                memberId, orderDate, endDate, pageable
            )
        }
        val orders = orderJpaEntities.content.map { it.toOrder() }
        return PageImpl(orders, pageable, orderJpaEntities.totalElements)
    }
}