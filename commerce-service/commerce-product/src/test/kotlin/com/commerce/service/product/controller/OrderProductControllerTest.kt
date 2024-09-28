package com.commerce.service.product.controller

import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.product.application.usecase.OrderProductUseCase
import com.commerce.service.product.application.usecase.dto.BeforeOrderProductDto
import com.commerce.service.product.controller.request.BeforeOrderRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal

@Import(OrderProductController::class, ObjectMapperConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(controllers = [OrderProductController::class])
class OrderProductControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var orderProductUseCase: OrderProductUseCase

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
    fun beforeOrder() {
        val request = BeforeOrderRequest(
            listOf(
                BeforeOrderRequest.IdAndQuantity(productId = 1, quantity = 2),
                BeforeOrderRequest.IdAndQuantity(productId = 3, quantity = 4)
            )
        )
        given(orderProductUseCase.getBeforeOrderProducts(request.toQuery())).willReturn(
            listOf(
                BeforeOrderProductDto(
                    productId = 1,
                    title = "[개발팀]국내도서 테스트 상품 (테스트 상품입니다.)",
                    coverImage = "https://shopping-phinf.pstatic.net/main_4964421/49644215637.20240806201630.jpg",
                    quantity = 12,
                    price = BigDecimal("353700"),
                    discountedPrice = BigDecimal("303804")
                )
            )
        )

        mockMvc.perform(
            post("/product/v1/products/order/before")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "product/v1/products/order/before",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("products").type(JsonFieldType.ARRAY).description("상품 목록"),
                        fieldWithPath("products[].productId").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("products[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.products").type(JsonFieldType.ARRAY).description("상품 목록"),
                        fieldWithPath("data.products[].productId").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("data.products[].title").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.products[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.products[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER).description("상품 금액"),
                        fieldWithPath("data.products[].discountedPrice").type(JsonFieldType.NUMBER)
                            .description("상품 할인된 금액"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }
}