package com.commerce.service.product.application.usecase.dto

import com.commerce.common.model.category.Category

class ProductCategoryInfoDto(
    val id: Long?,
    val parentId: Long?,
    val name: String,
    val depth: Int,
    val childCategories: List<ProductCategoryInfoDto>? = null,
) {
    companion object {
        fun from(categories: List<Category>): ProductCategoryInfoDto {

            val rootCategory = categories.first { it.parentId == 0L }
            return buildDto(rootCategory, categories)
        }

        private fun buildDto(category: Category, categories: List<Category>): ProductCategoryInfoDto {
            val childDtos = categories
                .filter { it.parentId == category.id }
                .map { buildDto(it, categories) }

            return ProductCategoryInfoDto(
                id = category?.id,
                parentId = category?.parentId,
                name = category.name,
                depth = category.depth,
                childCategories = if (childDtos.isEmpty()) null else childDtos
            )
        }
    }
}