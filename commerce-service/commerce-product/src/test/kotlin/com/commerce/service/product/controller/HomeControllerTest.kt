package com.commerce.service.product.controller

import com.commerce.common.model.category.CategoryDetail
import com.commerce.common.model.product.Product
import com.commerce.common.model.product.SaleStatus
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.product.application.usecase.HomeProductUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath
import org.springframework.restdocs.payload.PayloadDocumentation.responseFields
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.time.LocalDateTime

@Import(HomeController::class, ObjectMapperConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(controllers = [HomeController::class])
class HomeControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var homeProductUseCase: HomeProductUseCase

    @BeforeEach
    fun setUp(
        applicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun homeProducts() {
        given(homeProductUseCase.getHomeProducts(any())).willReturn(
            listOf(
                Product(
                    id = 1,
                    title = "책 1",
                    author = "저자",
                    price = BigDecimal("13000"),
                    discountedPrice = BigDecimal("12300"),
                    publisher = "출판사",
                    publishDate = LocalDateTime.now(),
                    isbn = "9791164384440",
                    description = "설명~~~~~길다길어",
                    pages = 123,
                    coverImage = "http://image.com",
                    previewLink = "https://search.shopping.naver.com/book/catalog/32497549635",
                    stockQuantity = 2790,
                    rating = 4.5,
                    status = SaleStatus.ON_SALE,
                    isHotNew = false,
                    isRecommend = false,
                    isBestseller = false,
                    category = CategoryDetail(
                        id = 1,
                        name = "국내 도서",
                        parentCategory = CategoryDetail.ParentCategory(
                            id = 2,
                            name = "소설"
                        )
                    )
                )
            )
        )

        mockMvc.perform(
            get("/product/v1/home/products")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "product/v1/home/products",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.hotNew").type(JsonFieldType.ARRAY).description("화제의 신간"),
                        fieldWithPath("data.hotNew[].id").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("data.hotNew[].title").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.hotNew[].author").type(JsonFieldType.STRING).description("상품 저자"),
                        fieldWithPath("data.hotNew[].publisher").type(JsonFieldType.STRING).description("상품 출판사"),
                        fieldWithPath("data.hotNew[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.hotNew[].categoryTitle").type(JsonFieldType.STRING).optional().description("상품 카테고리 표시 이름"),
                        fieldWithPath("data.recommend").type(JsonFieldType.ARRAY).description("추천 도서"),
                        fieldWithPath("data.recommend[].id").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("data.recommend[].title").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.recommend[].author").type(JsonFieldType.STRING).description("상품 저자"),
                        fieldWithPath("data.recommend[].publisher").type(JsonFieldType.STRING).description("상품 출판사"),
                        fieldWithPath("data.recommend[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.recommend[].categoryTitle").type(JsonFieldType.STRING).optional().description("상품 카테고리 표시 이름"),
                        fieldWithPath("data.bestseller").type(JsonFieldType.ARRAY).description("베스트 셀러"),
                        fieldWithPath("data.bestseller[].id").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("data.bestseller[].title").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.bestseller[].author").type(JsonFieldType.STRING).description("상품 저자"),
                        fieldWithPath("data.bestseller[].publisher").type(JsonFieldType.STRING).description("상품 출판사"),
                        fieldWithPath("data.bestseller[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.bestseller[].categoryTitle").type(JsonFieldType.STRING).optional().description("상품 카테고리 표시 이름"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }
}