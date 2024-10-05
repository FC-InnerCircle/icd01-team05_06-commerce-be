package com.commerce.service.product.controller

import com.commerce.common.model.product.HomeProductType
import com.commerce.common.response.CommonResponse
import com.commerce.service.product.application.usecase.HomeProductUseCase
import com.commerce.service.product.controller.response.HomeProductDto
import com.commerce.service.product.controller.response.HomeProductResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/product/v1/home")
class HomeController(
    private val homeProductUseCase: HomeProductUseCase
) {

    @GetMapping("/products")
    fun homeProducts(): CommonResponse<HomeProductResponse> {
        val homeProductResponse = HomeProductResponse(
            hotNew = homeProductUseCase.getHomeProducts(HomeProductType.HOT_NEW).map { HomeProductDto.from(it) },
            recommend = homeProductUseCase.getHomeProducts(HomeProductType.RECOMMEND).map { HomeProductDto.from(it) },
            bestseller = homeProductUseCase.getHomeProducts(HomeProductType.BESTSELLER).map { HomeProductDto.from(it) }
        )
        return CommonResponse.ok(homeProductResponse)
    }
}