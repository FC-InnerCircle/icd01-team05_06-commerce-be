package com.commerce.common.model.shopping_cart

class FakeShoppingCartRepository : ShoppingCartRepository {

    var autoIncrementId = 1L
    var data: MutableList<ShoppingCart> = mutableListOf()

    override fun save(shoppingCart: ShoppingCart) =
        if (shoppingCart.id > 0) {
            data = data.filter { it.id != shoppingCart.id }.toMutableList()
            data.add(shoppingCart)
            shoppingCart
        } else {
            val newShoppingCart = ShoppingCart(
                id = autoIncrementId++,
                memberId = shoppingCart.memberId,
                productId = shoppingCart.productId,
                quantity = shoppingCart.quantity
            )
            data.add(newShoppingCart)
            newShoppingCart
        }

    override fun findById(id: Long) = data.find { it.id == id }

    override fun findByMemberIdAndProductId(memberId: Long, productId: Long) = data.find {
        it.memberId == memberId && it.productId == productId
    }

    override fun deleteById(shoppingCartId: Long) {
        data.removeIf { it.id == shoppingCartId }
    }

    override fun deleteByMemberIdAndProductIdIn(memberId: Long, productIds: List<Long>) {
        TODO("Not yet implemented")
    }

    override fun findProducts(id: Long): List<ShoppingCartProduct> {
        TODO("Not yet implemented")
    }
}