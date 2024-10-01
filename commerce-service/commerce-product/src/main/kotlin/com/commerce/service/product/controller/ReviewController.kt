package com.commerce.service.product.controller

import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.dto.ReviewInfoDto
import com.commerce.service.product.controller.request.ReviewCreateRequest
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/v1/reviews")
class ReviewController(
    private val reviewUseCase: ReviewUseCase,
) {

    // 리뷰 select
    @GetMapping
    fun getReviewsByProductId(@RequestParam productId: Long): CommonResponse<ReviewInfoDto> {
        return CommonResponse.ok(reviewUseCase.getProductReviews(productId))
    }

    // 리뷰 작성
    @PostMapping
    fun addReviewToProduct(@RequestBody reviewCreateRequest: ReviewCreateRequest): CommonResponse<Long> {
        val reviewId = reviewUseCase.addReviewToProduct(reviewCreateRequest.toCommand())
        return CommonResponse.ok(reviewId);
    }

    // 상품 전체 평점 select



}