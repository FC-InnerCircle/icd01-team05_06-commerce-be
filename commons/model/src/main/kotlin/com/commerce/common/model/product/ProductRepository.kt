package com.commerce.common.model.product


interface ProductRepository {
    fun findByCategoryId(categoryId: Long, page: Int, size: Int): List<Product>
}