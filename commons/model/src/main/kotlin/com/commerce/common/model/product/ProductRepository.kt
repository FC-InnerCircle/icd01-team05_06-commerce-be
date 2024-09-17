package com.commerce.common.model.product


interface ProductRepository {

    fun findById(productId: Long): Product

    fun findByProductIdIn(ids: List<Long>): List<Product>

    fun findBySearchWord(searchWord: String?, categoryId: Long?, page: Int, size: Int): List<Product>
}