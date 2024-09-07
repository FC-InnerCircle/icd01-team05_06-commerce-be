package com.commerce.common.model.category

class CategoryDetail(
    val id: Long?,
    val name: String,
    val parentCategory: ParentCategory?,
) {

    data class ParentCategory(
        val id: Long?,
        val name: String,
    )
}