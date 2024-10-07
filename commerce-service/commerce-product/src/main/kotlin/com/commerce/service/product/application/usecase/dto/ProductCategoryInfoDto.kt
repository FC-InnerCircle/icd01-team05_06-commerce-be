package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.category.Category

class ProductCategoryInfoDto(
    val childCategories: List<ProductCategoryChildInfoDto>? = null,
) {
    class ProductCategoryChildInfoDto(
        val id: Long?,
        val parentId: Long?,
        val name: String,
        val depth: Int,
        val childCategories: List<ProductCategoryChildInfoDto>? = null,
    )

    companion object {
        fun from(categories: List<Category>): ProductCategoryInfoDto {

            val minDepth = categories.minOf { category -> category.depth }

            return ProductCategoryInfoDto(
                categories
                    .filter { it.depth == minDepth }
                    .map { buildDto(it, categories) }
                    .toList()
            )
        }

        private fun buildDto(category: Category, categories: List<Category>): ProductCategoryChildInfoDto {
            val childDtos = categories
                .filter { it.parentId == category.id }
                .map { buildDto(it, categories) }

            return ProductCategoryChildInfoDto(
                id = category?.id,
                parentId = category?.parentId,
                name = category.name,
                depth = category.depth,
                childCategories = if (childDtos.isEmpty()) null else childDtos
            )
        }
    }
}