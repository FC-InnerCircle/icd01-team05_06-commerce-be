package com.commerce.service.product.application.service

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.FakeProductRepository
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import com.commerce.service.product.application.usecase.query.BeforeOrderQuery
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderProductServiceTest {

    private lateinit var productRepository: FakeProductRepository
    private val orderProductService by lazy {
        OrderProductService(productRepository)
    };

    @BeforeEach
    fun setUp() {
        productRepository = FakeProductRepository()
    }

    @Test
    fun `재고 수량에 맞춰 금액을 곱하여 반환할 수 있다`() {
        productRepository.data = listOf(
            getTestProduct(id = 1, price = BigDecimal("1000"), discountedPrice = BigDecimal("500")),
            getTestProduct(id = 2, price = BigDecimal("2000"), discountedPrice = BigDecimal("1000"))
        ).toMutableList()

        val query = BeforeOrderQuery(
            listOf(
                BeforeOrderQuery.IdAndQuantity(
                    productId = 1,
                    quantity = 3
                ),
                BeforeOrderQuery.IdAndQuantity(
                    productId = 2,
                    quantity = 5
                )
            )
        )

        val result = orderProductService.getBeforeOrderProducts(query)

        assertThat(result.size).isEqualTo(2)
        assertThat(result[0].productId).isEqualTo(1)
        assertThat(result[0].price).isEqualTo(BigDecimal("3000"))
        assertThat(result[0].discountedPrice).isEqualTo(BigDecimal("1500"))
        assertThat(result[1].productId).isEqualTo(2)
        assertThat(result[1].price).isEqualTo(BigDecimal("10000"))
        assertThat(result[1].discountedPrice).isEqualTo(BigDecimal("5000"))
    }

    private fun getTestProduct(id: Long, price: BigDecimal, discountedPrice: BigDecimal) = Product(
        id = id,
        title = "제목",
        author = "작가",
        price = price,
        discountedPrice = discountedPrice,
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
        )
    )
}