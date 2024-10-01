package com.commerce.common.persistence.review

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "review")
class ReviewJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val content: String,
    @Column(precision = 38, scale = 2)
    val score: BigDecimal,
    val memberId: Long,
    val productId: Long,
    val orderProductId: Long? = null,

    @Column(name = "created_at")
    val createdAt: LocalDateTime,
    @Column(name = "updated_at")
    val updatedAt: LocalDateTime,
) {
    companion object {
        fun of(productId: Long, memberId: Long, content: String, score: BigDecimal, orderProductId: Long?): ReviewJpaEntity {
            return ReviewJpaEntity(
                id = null,
                content = content,
                score = score,
                memberId = memberId,
                productId = productId,
                orderProductId = orderProductId,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now(),
            )
        }
    }

}