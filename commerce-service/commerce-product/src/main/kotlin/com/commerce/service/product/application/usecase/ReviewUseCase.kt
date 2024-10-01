package com.commerce.service.product.application.usecase

import com.commerce.service.product.application.usecase.command.AddReviewCommand
import com.commerce.service.product.application.usecase.dto.ReviewInfoDto

interface ReviewUseCase {
    fun getProductReviews(productId: Long): ReviewInfoDto
    fun addReviewToProduct(addReviewCommand: AddReviewCommand): Long
}