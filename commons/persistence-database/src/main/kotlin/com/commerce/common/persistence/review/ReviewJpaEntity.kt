package com.commerce.common.persistence.review

import com.commerce.common.model.review.Review
import com.commerce.common.persistence.BaseTimeEntity
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "review")
class ReviewJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    val content: String,
    @Column(precision = 38, scale = 2)
    val score: BigDecimal,
    val memberId: Long,
    val productId: Long,
    val orderProductId: Long? = null,
) : BaseTimeEntity() {
    fun toModel() = Review(
        id = id,
        content = content,
        score = score,
        memberId = memberId,
        productId = productId,
        orderProductId = orderProductId,
        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    companion object {
        fun from(review: Review) = ReviewJpaEntity(
            id = review.id,
            content = review.content,
            score = review.score,
            memberId = review.memberId,
            productId = review.productId,
            orderProductId = review.orderProductId,
        )
    }

}