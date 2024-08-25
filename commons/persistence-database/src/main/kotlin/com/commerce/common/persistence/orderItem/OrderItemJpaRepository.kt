package com.commerce.common.persistence.orderItem

import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemJpaRepository : JpaRepository<OrderItemJpaEntity, Long>