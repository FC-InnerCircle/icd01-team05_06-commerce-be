package com.commerce.common.model.shopping_cart

interface ShoppingCartRepository {

    fun save(shoppingCart: ShoppingCart): ShoppingCart

    fun findById(id: Long): ShoppingCart?

    fun findByMemberIdAndProductId(memberId: Long, productId: Long): ShoppingCart?

    fun deleteById(shoppingCartId: Long)
}