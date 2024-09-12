package com.commerce.common.persistence.shopping_cart

import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ShoppingCartRepositoryImpl(
    private val shoppingCartJpaRepository: ShoppingCartJpaRepository
) : ShoppingCartRepository {

    override fun save(shoppingCart: ShoppingCart): ShoppingCart {
        return shoppingCartJpaRepository.save(ShoppingCartJpaEntity.from(shoppingCart)).toModel()
    }

    override fun findById(id: Long) = shoppingCartJpaRepository.findByIdOrNull(id)?.toModel()

    override fun findByMemberIdAndProductId(memberId: Long, productId: Long): ShoppingCart? {
        return shoppingCartJpaRepository.findByMemberIdAndProductId(memberId, productId)?.toModel()
    }

    override fun deleteById(shoppingCartId: Long) {
        shoppingCartJpaRepository.deleteById(shoppingCartId)
    }
}