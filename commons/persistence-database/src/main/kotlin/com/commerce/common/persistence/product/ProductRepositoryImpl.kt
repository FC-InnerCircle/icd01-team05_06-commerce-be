package com.commerce.common.persistence.product

import com.commerce.common.model.product.HomeProductType
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.product.ProductWithTag
import com.commerce.common.model.util.PaginationModel
import com.commerce.common.persistence.category.CategoryJpaEntity
import com.commerce.common.persistence.category.CategoryJpaRepository
import com.commerce.common.persistence.product_tag.ProductTagJpaRepository
import com.commerce.common.persistence.util.getOneBasedPageRequest
import com.commerce.common.persistence.util.toPaginationModel
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class ProductRepositoryImpl(
    private val productJpaRepository: ProductJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val productTagJpaRepository: ProductTagJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ProductRepository {

    override fun findByProductIdIn(ids: List<Long>): List<Product> {
        val productJpaEntities = productJpaRepository.findByIdIn(ids)

        val categoryIds = productJpaEntities.mapNotNull { it.categoryId }.distinct().toList()
        val categories = categoryJpaRepository.findByIdIn(categoryIds).associateBy(CategoryJpaEntity::id, CategoryJpaEntity::toProductModel)

        return productJpaEntities
            .map { product ->
                val category = product.categoryId?.let { categoryId -> categories[categoryId] }
                product.toModel(category)
            }
            .toList()
    }

    override fun findById(productId: Long): Product {
        val product = productJpaRepository.findById(productId)
            .orElseThrow { throw EntityNotFoundException("해당 제품이 존재하지 않습니다.") }

        val category = product.categoryId?.let { categoryId ->
            categoryJpaRepository.findById(categoryId)
                .map { it.toProductModel() }
                .orElse(null)
        }

        return product.toModel(category)
    }

    override fun findByIdWithTags(productId: Long): ProductWithTag {
        val product = productJpaRepository.findById(productId)
            .orElseThrow { throw EntityNotFoundException("해당 제품이 존재하지 않습니다.") }

        val category = product.categoryId?.let { categoryId ->
            categoryJpaRepository.findById(categoryId)
                .map { it.toProductModel() }
                .orElse(null)
        }

        val tags = productTagJpaRepository.findByProductId(productId).map { it.name }

        return ProductWithTag(product.toModel(category), tags)
    }

    override fun findBySearchWord(
        searchWord: String?,
        categoryId: Long?,
        homeProductType: HomeProductType?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        page: Int,
        size: Int
    ): PaginationModel<ProductWithTag> {

        val pageable = getOneBasedPageRequest(page, size)
        val pageResult = productJpaRepository.findPage(pageable) {
            select(
                entity(ProductJpaEntity::class),
            ).from(
                entity(ProductJpaEntity::class)
            ).whereAnd(
                searchWord?.let { path(ProductJpaEntity::title).like("%$searchWord%") },
                categoryId?.let { path(ProductJpaEntity::categoryId).eq(categoryId) },
                minPrice?.let { path(ProductJpaEntity::discountedPrice).greaterThanOrEqualTo(minPrice) },
                maxPrice?.let { path(ProductJpaEntity::discountedPrice).lessThanOrEqualTo(maxPrice) },
                homeProductType?.let {
                    when (it) {
                        HomeProductType.HOT_NEW -> path(ProductJpaEntity::isHotNew).eq(true)
                        HomeProductType.RECOMMEND -> path(ProductJpaEntity::isRecommend).eq(true)
                        HomeProductType.BESTSELLER -> path(ProductJpaEntity::isBestseller).eq(true)
                    }
                },
            )
        }

        val categoryIds = pageResult.content.mapNotNull { it!!.categoryId }.distinct().toList()
        val categories = categoryJpaRepository.findByIdIn(categoryIds)
            .associateBy(CategoryJpaEntity::id, CategoryJpaEntity::toProductModel)

        val productIds = pageResult.content.mapNotNull { it!!.id }.distinct().toList()
        val productTags = productTagJpaRepository.findByProductIdIn(productIds).groupBy({ it.productId }) { it.name }

        return pageResult.toPaginationModel({ product ->
            val category = product!!.categoryId?.let { categoryId -> categories[categoryId] }
            ProductWithTag(product.toModel(category), productTags[product.id]!!)
        })
    }

    override fun findByHomeProductType(homeProductType: HomeProductType): List<Product> {
        val jpql = jpql {
            selectNew<ProductAndCategoryEntities>(
                entity(ProductJpaEntity::class),
                entity(CategoryJpaEntity::class)
            ).from(
                entity(ProductJpaEntity::class),
                leftJoin(CategoryJpaEntity::class).on(path(ProductJpaEntity::categoryId).eq(path(CategoryJpaEntity::id)))
            ).where(
                when (homeProductType) {
                    HomeProductType.HOT_NEW -> path(ProductJpaEntity::isHotNew).eq(true)
                    HomeProductType.RECOMMEND -> path(ProductJpaEntity::isRecommend).eq(true)
                    HomeProductType.BESTSELLER -> path(ProductJpaEntity::isBestseller).eq(true)
                }
            )
        }

        val query = entityManager.createQuery(jpql, jpqlRenderContext)
        val result = query.resultList

        return result.map {
            it.productJpaEntity.toModel(it.categoryJpaEntity?.toProductModel())
        }
    }

    override fun save(product: Product): Product {
        val savedProduct = productJpaRepository.save(product.toJpaEntity())
        return savedProduct.toModel(product.category)
    }
}