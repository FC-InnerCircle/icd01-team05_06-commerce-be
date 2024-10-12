package com.commerce.common.model.product

import com.commerce.common.model.util.PaginationModel


interface ProductRepository {

    fun findById(productId: Long): Product

    fun findByProductIdIn(ids: List<Long>): List<Product>

    fun findBySearchWord(searchWord: String?, categoryId: Long?, homeProductType: HomeProductType?, page: Int, size: Int): PaginationModel<Product>

    fun findByHomeProductType(homeProductType: HomeProductType): List<Product>

    fun save(product: Product): Product
}