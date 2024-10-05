package com.commerce.service.product.controller

import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.controller.request.ReviewCreateRequest
import com.commerce.service.product.controller.response.ReviewCreateResponse
import com.commerce.service.product.controller.response.ReviewInfoDto
import com.commerce.service.product.controller.response.ReviewInfoResponse
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/product/v1/reviews")
class ReviewController(
    private val reviewUseCase: ReviewUseCase,
) {

    // 리뷰 select
    @GetMapping
    fun getReviewsByProductId(@RequestParam productId: Long): CommonResponse<ReviewInfoResponse> {
        val reviews = reviewUseCase.getProductReviews(productId).map {
            ReviewInfoDto(
                reviewId = it.id,
                content = it.content,
                score = it.score,
                email = it.email,
                productId = it.productId,
                updatedAt = it.updatedAt,
                orderProductId = null
            )
        }

        return CommonResponse.ok(ReviewInfoResponse(reviews))
    }

    // 리뷰 작성
    @PostMapping
    fun addReviewToProduct(@RequestBody reviewCreateRequest: ReviewCreateRequest): CommonResponse<ReviewCreateResponse> {
        val reviewId = reviewUseCase.addReviewToProduct(reviewCreateRequest.toCommand())
        return CommonResponse.ok(ReviewCreateResponse.of(reviewId));
    }

    // 상품 전체 평점 select



}