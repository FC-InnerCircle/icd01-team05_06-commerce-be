package com.commerce.service.product.controller

import com.commerce.common.model.member.Member
import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.controller.request.ReviewCreateRequest
import com.commerce.service.product.controller.response.ReviewCreateResponse
import com.commerce.service.product.controller.response.ReviewInfoDto
import com.commerce.service.product.controller.response.ReviewInfoResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product/v1/reviews")
class ReviewController(
    private val reviewUseCase: ReviewUseCase,
) {

    @GetMapping
    fun getReviewsByProductId(@RequestParam productId: Long): CommonResponse<ReviewInfoResponse> {
        val reviews = reviewUseCase.getProductReviews(productId).map {
            ReviewInfoDto(
                reviewId = it.id,
                content = it.content,
                score = it.score,
                email = it.email,
                productId = it.productId,
                createdAt = it.createdAt,
                lastModifiedByUserAt = it.lastModifiedByUserAt,
                orderProductId = null
            )
        }

        return CommonResponse.ok(ReviewInfoResponse(reviews))
    }

    @PostMapping
    fun addReviewToProduct(@AuthenticationPrincipal member: Member, @RequestBody reviewCreateRequest: ReviewCreateRequest): CommonResponse<ReviewCreateResponse> {
        val reviewId = reviewUseCase.addReviewToProduct(reviewCreateRequest.toCommand(member.email))
        return CommonResponse.ok(ReviewCreateResponse.of(reviewId))
    }
}