package com.commerce.common.persistence.shopping_cart

import com.commerce.common.model.shopping_cart.ShoppingCart
import com.commerce.common.model.shopping_cart.ShoppingCartProduct
import com.commerce.common.model.shopping_cart.ShoppingCartRepository
import com.commerce.common.persistence.product.ProductJpaEntity
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.querymodel.jpql.expression.Expressions
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class ShoppingCartRepositoryImpl(
    private val jpqlRenderContext: JpqlRenderContext,
    private val entityManager: EntityManager,
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

    override fun findProducts(id: Long): List<ShoppingCartProduct> {
        val jpql = jpql {
            selectNew<ShoppingCartProduct>(
                path(ShoppingCartJpaEntity::id),
                path(ProductJpaEntity::id),
                path(ProductJpaEntity::title),
                path(ProductJpaEntity::coverImage),
                path(ShoppingCartJpaEntity::quantity),
                Expressions.times(path(ProductJpaEntity::price), path(ShoppingCartJpaEntity::quantity)),
                Expressions.times(path(ProductJpaEntity::discountedPrice), path(ShoppingCartJpaEntity::quantity)),
            ).from(
                entity(ShoppingCartJpaEntity::class),
                join(ProductJpaEntity::class).on(path(ShoppingCartJpaEntity::productId).equal(path(ProductJpaEntity::id)))
            ).where(
                path(ShoppingCartJpaEntity::memberId).eq(id)
            ).orderBy(
                path(ShoppingCartJpaEntity::createdAt).desc()
            )
        }
        val query = entityManager.createQuery(jpql, jpqlRenderContext)
        return query.resultList
    }
}