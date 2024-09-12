package com.commerce.common.persistence.shopping_cart

import org.springframework.data.jpa.repository.JpaRepository

interface ShoppingCartJpaRepository : JpaRepository<ShoppingCartJpaEntity, Long> {

    fun findByMemberIdAndProductId(memberId: Long, productId: Long): ShoppingCartJpaEntity?
}