package com.commerce.common.persistence.product

import com.commerce.common.model.product.HomeProductType
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.util.PaginationInfo
import com.commerce.common.model.util.PaginationModel
import com.commerce.common.persistence.category.CategoryJpaEntity
import com.commerce.common.persistence.category.CategoryJpaRepository
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Repository
import kotlin.math.ceil

@Repository
class ProductRepositoryImpl (
    private val productJpaRepository: ProductJpaRepository,
    private val categoryJpaRepository: CategoryJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ProductRepository {

    override fun findByProductIdIn(ids: List<Long>): List<Product> {
        return productJpaRepository.findByIdIn(ids)
            .map { product ->
                val category = product.categoryId?.let { categoryId ->
                    categoryJpaRepository.findById(categoryId)
                        .map { it.toProductModel() }
                        .orElse(null)
                }
                product.toModel(category)
            }
            .toList()
    }

    override fun findById(productId: Long): Product {
        val product = productJpaRepository.findById(productId)
            .orElseThrow{ throw EntityNotFoundException("해당 제품이 존재하지 않습니다.") }

        val category = product.categoryId?.let { categoryId ->
            categoryJpaRepository.findById(categoryId)
                .map { it.toProductModel() }
                .orElse(null)
        }

        return product.toModel(category)
    }

    override fun findBySearchWord(searchWord: String?, categoryId: Long?, page: Int, size: Int): PaginationModel<Product> {

        val jpql = jpql {
            select(
                entity(ProductJpaEntity::class),
            ).from(
                entity(ProductJpaEntity::class)
            ).whereAnd(
                searchWord?.let { path(ProductJpaEntity::title).like("%$searchWord%") },
                categoryId?.let { path(ProductJpaEntity::categoryId).eq(categoryId) }
            )
        }

        val countJpql = jpql {
            select(
                count(entity(ProductJpaEntity::class))
            ).from(
                entity(ProductJpaEntity::class)
            ).whereAnd(
                searchWord?.let { path(ProductJpaEntity::title).like("%$searchWord%") },
                categoryId?.let { path(ProductJpaEntity::categoryId).eq(categoryId) }
            )
        }

        val totalCount = entityManager.createQuery(countJpql, jpqlRenderContext).singleResult
        val totalPage = ceil(totalCount.toDouble() / size).toInt()

        val query = entityManager.createQuery(jpql, jpqlRenderContext)

        // 페이징 처리
        query.firstResult = (page - 1) * size
        query.maxResults = size

      val resultResult = query.resultList.map { product ->
            val category = product.categoryId?.let { categoryId ->
                categoryJpaRepository.findById(categoryId)
                    .map{ it.toProductModel() }
                    .orElse(null)
            }
            product.toModel(category)
        }
            .toList()

        val paginationInfo = PaginationInfo(
            currentPage = page,
            totalCount = totalCount,
            totalPage = totalPage,
            pageSize = size,
            hasNextPage = page < totalPage,
            hasPreviousPage = page > 1,
        )

        return PaginationModel(
            data = resultResult,
            pagination = paginationInfo,
        )
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
        val savedProduct = productJpaRepository.save(product.toJpaEntity(product.id ?: 0))
        val category = savedProduct.categoryId?.let { categoryId ->
            categoryJpaRepository.findById(categoryId)
                .map { it.toProductModel() }
                .orElse(null)
        }
        return savedProduct.toModel(category)
    }
}