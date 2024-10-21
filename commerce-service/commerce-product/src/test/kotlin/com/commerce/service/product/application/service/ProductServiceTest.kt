package com.commerce.service.product.application.service

import com.commerce.common.model.category.CategoryRepository
import com.commerce.common.model.category.FakeCategoryRepository
import com.commerce.common.model.product.FakeProductRepository
import com.commerce.common.model.product.ProductRepository
import com.commerce.common.model.product.SaleStatus
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.math.BigDecimal

class ProductServiceTest {

    private lateinit var productRepository: ProductRepository
    private lateinit var categoryRepository: CategoryRepository
    private val productService by lazy {
        ProductService(productRepository, categoryRepository)
    }

    @BeforeEach
    fun setUp() {
        productRepository = FakeProductRepository()
        categoryRepository = FakeCategoryRepository()
    }

    @Test
    fun `카테고리 전체 리스트를 출력한다`() {
        // given
        // when

        val categories = categoryRepository.findAll()
        // then
        assertThat(categories).hasSize(2)
        assertThat(categories[0].id).isEqualTo(1L)
        assertThat(categories[0].parentId).isEqualTo(0L)
        assertThat(categories[0].name).isEqualTo("국내도서")
        assertThat(categories[0].depth).isEqualTo(1)

        assertThat(categories[1].id).isEqualTo(2L)
        assertThat(categories[1].parentId).isEqualTo(0L)
        assertThat(categories[1].name).isEqualTo("해외도서")
        assertThat(categories[1].depth).isEqualTo(1)
    }

    @Test
    fun `product를 검색한다`() {
        // when
        var products = productRepository.findBySearchWord(
            searchWord = null,
            categoryId = 2L,
            homeProductType = null,
            minPrice = null,
            maxPrice = null,
            page = 1,
            size = 20
        )

        // then
        assertThat(products.data).hasSize(1)
        assertThat(products.data[0].product.title).isEqualTo("제목")
        assertThat(products.data[0].product.author).isEqualTo("작가")
        assertThat(products.data[0].product.price).isEqualTo(BigDecimal("10000"))
        assertThat(products.data[0].product.discountedPrice).isEqualTo(BigDecimal("1000"))
        assertThat(products.data[0].product.publisher).isEqualTo("퍼블리셔")
        assertThat(products.data[0].product.isbn).isEqualTo("1111")
        assertThat(products.data[0].product.status).isEqualTo(SaleStatus.ON_SALE)
        assertThat(products.data[0].product.category?.id).isEqualTo(2L)
        assertThat(products.data[0].product.category?.name).isEqualTo("국내도서")
    }

    @Test
    fun `product id로 product 상세보기`() {
        // when
        val product = productRepository.findById(2L)

        // then
        assertThat(product).isNotNull()
        assertThat(product.title).isEqualTo("제목")
        assertThat(product.author).isEqualTo("작가")
        assertThat(product.price).isEqualTo(BigDecimal("10000"))
        assertThat(product.discountedPrice).isEqualTo(BigDecimal("1000"))
        assertThat(product.publisher).isEqualTo("퍼블리셔")
        assertThat(product.isbn).isEqualTo("1111")
        assertThat(product.status).isEqualTo(SaleStatus.ON_SALE)
        assertThat(product.category?.id).isEqualTo(3L)
        assertThat(product.category?.name).isEqualTo("해외도서")
    }

    @Test
    fun `product id가 없을 시 exception이 발생한다`() {
        // when // then
        assertThatThrownBy{ productRepository.findById(99L) }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("제품이 존재하지 않습니다.")
    }
}