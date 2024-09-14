package com.commerce.service.order.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.shopping_cart.ShoppingCartProduct
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.order.applicaton.usecase.ShoppingCartUseCase
import com.commerce.service.order.applicaton.usecase.dto.ShoppingCartListDto
import com.commerce.service.order.config.SecurityConfig
import com.commerce.service.order.controller.request.PatchShoppingCartRequest
import com.commerce.service.order.controller.request.PostShoppingCartRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal

@Import(ShoppingCartController::class, ObjectMapperConfig::class, JwtAuthenticationFilter::class)
@WebMvcTest(ShoppingCartController::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
class ShoppingCartControllerTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenUseCase: TokenUseCase

    @MockBean
    private lateinit var memberRepository: MemberRepository

    @MockBean
    private lateinit var shoppingCartUseCase: ShoppingCartUseCase

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val testAccessToken =
        "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiaWF0IjoxNzI0NTIwNDc5LCJleHAiOjE3MjU3MzAwNzl9.i1WjcNXU2wBYjikGu5u0r41XmciafAfaMF3nNheb9cc7TUpai-tnMZCg3NUcTWP9"
    private val testMember = Member(
        id = 1,
        email = "commerce@example.com",
        password = "123!@#qwe",
        name = "홍길동",
        phone = "01012345678"
    )

    @BeforeEach
    fun setUp(
        applicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()

        // JwtAuthenticationFilter
        given(
            tokenUseCase.getTokenSubject(
                testAccessToken,
                TokenType.ACCESS_TOKEN
            )
        ).willReturn(testMember.id.toString())
        given(memberRepository.findById(testMember.id)).willReturn(testMember)
    }

    @Test
    fun postTest() {
        val request = PostShoppingCartRequest(
            productId = 1,
            quantity = 2
        )

        mockMvc.perform(
            post("/shopping-carts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "shopping-carts-post",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun patchTest() {
        val request = PatchShoppingCartRequest(
            quantity = 2
        )

        mockMvc.perform(
            patch("/shopping-carts/{shoppingCartId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "shopping-carts-patch",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("shoppingCartId").description("장바구니 ID")
                    ),
                    requestFields(
                        fieldWithPath("quantity").type(JsonFieldType.NUMBER).description("수량"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun deleteTest() {
        mockMvc.perform(
            delete("/shopping-carts/{shoppingCartId}", 1)
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "shopping-carts-delete",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    pathParameters(
                        parameterWithName("shoppingCartId").description("장바구니 ID")
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun getList() {
        given(shoppingCartUseCase.getList(testMember)).willReturn(ShoppingCartListDto(
            listOf(ShoppingCartProduct(
                shoppingCartId = 3,
                productId = 1,
                title = "[개발팀]국내도서 테스트 상품 (테스트 상품입니다.)",
                coverImage = "https://shopping-phinf.pstatic.net/main_4964421/49644215637.20240806201630.jpg",
                quantity = 12,
                price = BigDecimal("353700"),
                discountedPrice = BigDecimal("303804")
            ))
        ))

        mockMvc.perform(
            get("/shopping-carts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "shopping-carts",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.products").type(JsonFieldType.ARRAY).description("상품 목록"),
                        fieldWithPath("data.products[].shoppingCartId").type(JsonFieldType.NUMBER).description("장바구니 고유번호"),
                        fieldWithPath("data.products[].productId").type(JsonFieldType.NUMBER).description("상품 고유번호"),
                        fieldWithPath("data.products[].title").type(JsonFieldType.STRING).description("상품 이름"),
                        fieldWithPath("data.products[].coverImage").type(JsonFieldType.STRING).description("상품 이미지"),
                        fieldWithPath("data.products[].quantity").type(JsonFieldType.NUMBER).description("수량"),
                        fieldWithPath("data.products[].price").type(JsonFieldType.NUMBER).description("상품 금액"),
                        fieldWithPath("data.products[].discountedPrice").type(JsonFieldType.NUMBER).description("상품 할인된 금액"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }
}