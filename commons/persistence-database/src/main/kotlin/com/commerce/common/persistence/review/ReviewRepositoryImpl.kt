package com.commerce.common.persistence.review

import com.commerce.common.model.review.Review
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.persistence.member.MemberJpaEntity
import com.commerce.common.persistence.member.MemberJpaRepository
import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.jpql.JpqlRenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import java.math.BigDecimal

@Repository
class ReviewRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository,
    private val reviewJpaRepository: ReviewJpaRepository,
    private val entityManager: EntityManager,
    private val jpqlRenderContext: JpqlRenderContext,
) : ReviewRepository{

    override fun findByProductId(productId: Long): List<Review> {
        val jpql = jpql {
            selectNew<ReviewCustomDto>(
                path(ReviewJpaEntity::id),
                path(ReviewJpaEntity::content),
                path(ReviewJpaEntity::score),
                path(MemberJpaEntity::email),
                path(ReviewJpaEntity::productId),
                path(ReviewJpaEntity::createdAt),
                path(ReviewJpaEntity::updatedAt),
            ).from(
                entity(ReviewJpaEntity::class),
                join(MemberJpaEntity::class).on(path(MemberJpaEntity::id).equal(path(ReviewJpaEntity::memberId))),
            ).where(
                path(ReviewJpaEntity::productId).eq(productId)
            )
        }

        val query = entityManager.createQuery(jpql, jpqlRenderContext)

        return query.resultList.map { review ->
            review.toModel()
        }.toList()
    }

    override fun addReviewToProduct(
        productId: Long,
        memberId: Long,
        content: String,
        score: BigDecimal,
        orderProductId: Long?
    ): Long {

        val newReview = ReviewJpaEntity.of(productId, memberId, content, score, orderProductId)
        reviewJpaRepository.save(newReview)

        return newReview.id!!
    }
}