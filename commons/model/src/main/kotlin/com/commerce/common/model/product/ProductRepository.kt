package com.commerce.common.model.product

import com.commerce.common.model.util.PaginationModel
import java.math.BigDecimal


interface ProductRepository {

    fun findById(productId: Long): Product

    fun findByIdWithTags(productId: Long): ProductWithTag

    fun findByProductIdIn(ids: List<Long>): List<Product>

    fun findBySearchWord(searchWord: String?, categoryId: Long?, homeProductType: HomeProductType?, minPrice: BigDecimal?, maxPrice: BigDecimal?, page: Int, size: Int): PaginationModel<ProductWithTag>

    fun findByHomeProductType(homeProductType: HomeProductType): List<Product>

    fun save(product: Product): Product
}