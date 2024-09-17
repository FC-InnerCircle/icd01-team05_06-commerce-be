package com.commerce.common.model.product


interface ProductRepository {
    fun findByCategoryId(categoryId: Long, page: Int, size: Int): List<Product>

    fun findByProductIdIn(ids: List<Long>): List<Product>

    fun findById(productId: Long): Product
    fun findBySearchWord(keyword: String?, categoryId: Long?, page: Int, size: Int): List<Product>
}