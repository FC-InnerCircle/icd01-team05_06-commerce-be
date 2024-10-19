package com.commerce.service.product.controller.request

import com.commerce.service.product.application.usecase.command.UpdateReviewCommand
import java.math.BigDecimal

data class ReviewUpdateRequest(
    val content: String,
    val score: BigDecimal,
) {
    fun toCommand(): UpdateReviewCommand {
        return UpdateReviewCommand(
            content = content,
            score = score,
        )
    }
}