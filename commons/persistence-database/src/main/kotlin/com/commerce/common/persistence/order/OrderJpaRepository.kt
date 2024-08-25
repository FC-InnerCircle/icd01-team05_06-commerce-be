package com.commerce.common.persistence.order

import com.commerce.common.persistence.order.OrderJpaEntity as Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaRepository : JpaRepository<Order, Long>