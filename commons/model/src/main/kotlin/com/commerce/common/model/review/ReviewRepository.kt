package com.commerce.common.model.review

import java.math.BigDecimal

interface ReviewRepository {

    fun findByProductId(productId: Long): List<Review>
    fun addReviewToProduct(productId: Long, memberId: Long, content: String, score: BigDecimal, orderProductId: Long?): Long
}