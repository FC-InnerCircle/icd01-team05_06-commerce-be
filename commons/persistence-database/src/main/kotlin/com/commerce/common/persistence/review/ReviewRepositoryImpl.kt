package com.commerce.common.persistence.review

import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.model.review.ReviewWithProduct
import com.commerce.common.persistence.member.MemberJpaEntity
import com.commerce.common.persistence.product.ProductJpaEntity
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository

@Repository
class ReviewRepositoryImpl(
    private val reviewJpaRepository: ReviewJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ReviewRepository{

    override fun findByProductIdOrderByCreatedAtDesc(productId: Long): List<ReviewWithMember> {
        val jpql = jpql {
            selectNew<ReviewWithMember>(
                path(ReviewJpaEntity::id),
                path(ReviewJpaEntity::content),
                path(ReviewJpaEntity::score),
                path(MemberJpaEntity::email),
                path(ReviewJpaEntity::productId),
                path(ReviewJpaEntity::createdAt),
                path(ReviewJpaEntity::lastModifiedByUserAt),
            ).from(
                entity(ReviewJpaEntity::class),
                join(MemberJpaEntity::class).on(path(MemberJpaEntity::id).equal(path(ReviewJpaEntity::memberId))),
            ).where(
                path(ReviewJpaEntity::productId).eq(productId)
            ).orderBy(
                path(ReviewJpaEntity::createdAt).desc()
            )
        }

        val query = entityManager.createQuery(jpql, jpqlRenderContext)
        return query.resultList
    }

    override fun save(review: Review): Review {
        return reviewJpaRepository.save(ReviewJpaEntity.from(review)).toModel()
    }

    override fun findByMemberIdOrderByCreatedAtDesc(memberId: Long): List<ReviewWithProduct> {
        val jpql = jpql {
            selectNew<ReviewWithProduct>(
                path(ReviewJpaEntity::id),
                path(ReviewJpaEntity::content),
                path(ReviewJpaEntity::score),
                path(ReviewJpaEntity::productId),
                path(ProductJpaEntity::title),
                path(ProductJpaEntity::author),
                path(ProductJpaEntity::publisher),
                path(ProductJpaEntity::coverImage),
                path(ReviewJpaEntity::createdAt),
                path(ReviewJpaEntity::lastModifiedByUserAt),
            ).from(
                entity(ReviewJpaEntity::class),
                join(ProductJpaEntity::class).on(path(ProductJpaEntity::id).equal(path(ReviewJpaEntity::productId))),
            ).where(
                path(ReviewJpaEntity::memberId).eq(memberId)
            ).orderBy(
                path(ReviewJpaEntity::createdAt).desc()
            )
        }

        val query = entityManager.createQuery(jpql, jpqlRenderContext)
        return query.resultList
    }
}