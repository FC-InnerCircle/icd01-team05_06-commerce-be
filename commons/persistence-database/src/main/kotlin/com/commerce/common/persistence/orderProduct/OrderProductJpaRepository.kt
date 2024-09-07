package com.commerce.common.persistence.orderProduct

import org.springframework.data.jpa.repository.JpaRepository

interface OrderProductJpaRepository : JpaRepository<OrderProductJpaEntity, Long>