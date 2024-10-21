package com.commerce.common.model.product

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.util.PaginationInfo
import com.commerce.common.model.util.PaginationModel
import java.math.BigDecimal
import java.time.LocalDate

class FakeProductRepository : ProductRepository {

    var autoIncrementId = 1L
    var data: MutableList<Product> = mutableListOf()

    override fun findBySearchWord(searchWord: String?, categoryId: Long?, homeProductType: HomeProductType?, minPrice: BigDecimal?, maxPrice: BigDecimal?, page: Int, size: Int): PaginationModel<ProductWithTag> {
        initData()
        val resultList = data.filter { product ->
            categoryId?.let { it == product.category?.id } ?: true
        }.filter { product ->
            searchWord?.let { product.title.contains(it) } ?: true
        }.filter { product ->
            minPrice?.let { product.discountedPrice >= it } ?: true
        }.filter { product ->
            maxPrice?.let { product.discountedPrice <= it } ?: true
        }.filter { product ->
            searchWord?.let { product.title.contains(it) } ?: true
        }.filter { product ->
            homeProductType?.let {
                when (homeProductType) {
                    HomeProductType.HOT_NEW -> product.isHotNew
                    HomeProductType.RECOMMEND -> product.isRecommend
                    HomeProductType.BESTSELLER -> product.isBestseller
                }
            } ?: true
        }
        val pagination = PaginationInfo(
            currentPage = 0,
            totalPage = 1,
            pageSize = 1,
            totalCount = resultList.size.toLong(),
            hasNextPage = true,
            hasPreviousPage = true,
        )

        return PaginationModel(
            data = resultList.map { ProductWithTag(it, emptyList()) },
            pagination = pagination
        )
    }

    override fun findByProductIdIn(ids: List<Long>): List<Product> {
        return data.filter { ids.contains(it.id) }
    }

    override fun findById(productId: Long): Product {
        initData()
        var data = data.find { productId == it.id }

        if (data == null) {
            throw IllegalArgumentException("제품이 존재하지 않습니다.")
        }
        return data
    }

    override fun findByIdWithTags(productId: Long): ProductWithTag {
        TODO("Not yet implemented")
    }

    fun initData() {
        val command1 = Product(
            id = autoIncrementId++,
            title = "제목",
            author = "작가",
            price = BigDecimal("10000"),
            discountedPrice = BigDecimal("1000"),
            publisher = "퍼블리셔",
            publishDate = LocalDate.now(),
            isbn = "1111",
            description = "설명",
            pages = 300,
            coverImage = "커버이미지",
            previewLink = "링크",
            stockQuantity = 100,
            rating = 5.5,
            status = SaleStatus.ON_SALE,
            isHotNew = true,
            isRecommend = true,
            isBestseller = true,
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
            publishDate = LocalDate.now(),
            isbn = "1111",
            description = "설명",
            pages = 300,
            coverImage = "커버이미지",
            previewLink = "링크",
            stockQuantity = 100,
            rating = 5.5,
            status = SaleStatus.ON_SALE,
            isHotNew = true,
            isRecommend = true,
            isBestseller = true,
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

    override fun findByHomeProductType(homeProductType: HomeProductType): List<Product> {
        TODO("Not yet implemented")
    }

    override fun save(product: Product): Product {
        TODO("Not yet implemented")
    }
}