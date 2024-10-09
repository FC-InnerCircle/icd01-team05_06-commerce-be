package com.commerce.service.product.controller.request

import com.commerce.service.product.application.usecase.command.AddReviewCommand
import java.math.BigDecimal

data class ReviewCreateRequest(
    val productId: Long,
    val content: String,
    val score: BigDecimal,
) {
    fun toCommand(email: String): AddReviewCommand {
        return AddReviewCommand(
            productId = productId,
            email = email,
            content = content,
            score = score,
        )
    }
}