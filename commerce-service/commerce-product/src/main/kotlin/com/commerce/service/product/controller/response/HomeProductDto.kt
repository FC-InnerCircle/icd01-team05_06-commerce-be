package com.commerce.service.product.controller.response

import com.commerce.common.model.product.Product

data class HomeProductDto(
    val id: Long,
    val title: String,
    val author: String,
    val publisher: String,
    val coverImage: String,
    val categoryTitle: String?
) {
    companion object {
        fun from(product: Product) = with(product) {
            HomeProductDto(
                id = id!!,
                title = title,
                author = author,
                publisher = publisher,
                coverImage = coverImage,
                categoryTitle = category?.let { category ->
                    val parent = category.parentCategory?.let { parentCategory ->
                        "${parentCategory.name} > "
                    } ?: ""
                    "${parent}${category.name}"
                }
            )
        }
    }
}