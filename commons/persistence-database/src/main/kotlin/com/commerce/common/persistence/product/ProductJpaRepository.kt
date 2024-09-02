package com.commerce.common.persistence.product

import org.springframework.data.jpa.repository.JpaRepository

interface ProductJpaRepository : JpaRepository<ProductJpaEntity, Long>