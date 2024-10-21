package com.commerce.common.model.product

import com.commerce.common.model.util.PaginationModel
import java.math.BigDecimal


interface ProductRepository {

    fun findById(productId: Long): Product

    fun findByProductIdIn(ids: List<Long>): List<Product>

    fun findBySearchWord(searchWord: String?, categoryId: Long?, homeProductType: HomeProductType?, minPrice: BigDecimal?, maxPrice: BigDecimal?, page: Int, size: Int): PaginationModel<Product>

    fun findByHomeProductType(homeProductType: HomeProductType): List<Product>

    fun save(product: Product): Product
}