package com.mock

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.product.SaleStatus
import jakarta.persistence.EntityNotFoundException
import java.math.BigDecimal
import java.time.LocalDateTime

class FakeProductRepository : ProductRepository{

    var autoIncrementId = 1L
    var data: MutableList<Product> = mutableListOf()

    override fun findBySearchWord(searchWord: String?, categoryId: Long?, page: Int, size: Int): List<Product> {
        initData()
        return data.filter { categoryId == it.category?.id }
    }

    override fun findByProductIdIn(ids: List<Long>): List<Product> {
        return data.filter { ids.contains(it.id) }
    }

    override fun findById(productId: Long): Product {
        initData()
        var data = data.find { productId == it.id }

        if (data == null) {
            throw EntityNotFoundException("제품이 존재하지 않습니다.")
        }
        return data
    }

    fun initData() {
        val command1 = Product(
            id = autoIncrementId++,
            title = "제목",
            author = "작가",
            price = BigDecimal("10000"),
            discountedPrice = BigDecimal("1000"),
            publisher = "퍼블리셔",
            publishDate = LocalDateTime.now(),
            isbn = "1111",
            description = "설명",
            pages = 300,
            coverImage = "커버이미지",
            previewLink = "링크",
            stockQuantity = 100,
            rating = 5.5,
            status = SaleStatus.ON_SALE,
            category = CategoryDetail(
                id = 2L,
                name = "국내도서",
                parentCategory = CategoryDetail.ParentCategory(
                    id = 0L,
                    name = "전체"
                )
            ),
        )
        val command2 = Product(
            id = autoIncrementId++,
            title = "제목",
            author = "작가",
            price = BigDecimal("10000"),
            discountedPrice = BigDecimal("1000"),
            publisher = "퍼블리셔",
            publishDate = LocalDateTime.now(),
            isbn = "1111",
            description = "설명",
            pages = 300,
            coverImage = "커버이미지",
            previewLink = "링크",
            stockQuantity = 100,
            rating = 5.5,
            status = SaleStatus.ON_SALE,
            category = CategoryDetail(
                id = 3L,
                name = "해외도서",
                parentCategory = CategoryDetail.ParentCategory(
                    id = 0L,
                    name = "전체"
                )
            ),
        )
        data.add(command1)
        data.add(command2)
    }
}