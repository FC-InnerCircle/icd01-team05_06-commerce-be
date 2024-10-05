package com.commerce.service.product.controller

import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.model.review.ReviewWithMember
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.commerce.service.product.application.usecase.command.AddReviewCommand
import com.commerce.service.product.controller.request.ReviewCreateRequest
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.context.WebApplicationContext
import java.math.BigDecimal
import java.time.LocalDateTime

@Import(ReviewController::class, ObjectMapperConfig::class)
@ExtendWith(RestDocumentationExtension::class)
@WebMvcTest(controllers = [ReviewController::class])
class ReviewControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var reviewUseCase: ReviewUseCase

    @MockBean
    private lateinit var reviewRepository: ReviewRepository

    @MockBean
    private lateinit var memberRepository: MemberRepository

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
    fun getReviewsByProductId() {
        val productId = 1L
        given(reviewUseCase.getProductReviews(productId))
            .willReturn(
            listOf(
                ReviewWithMember(
                    id = 1L,
                    content = "이 책 너무 재밌어요",
                    score = BigDecimal(5),
                    email = "abc@naver.com",
                    productId = 1L,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                ),
                ReviewWithMember(
                    id = 2L,
                    content = "이 책 재밌긴 한데 아쉬워요",
                    score = BigDecimal(3),
                    email = "bbb@naver.com",
                    productId = 1L,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                ),
                ReviewWithMember(
                    id = 3L,
                    content = "어려워요 읽다 말았어요",
                    score = BigDecimal(1),
                    email = "ddd@naver.com",
                    productId = 1L,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now(),
                ),
            )
        )
        val queryParams = LinkedMultiValueMap<String, String>()
        queryParams["productId"] = "1"


            mockMvc.perform(get("/product/v1/reviews")
                .params(queryParams))
                .andExpect(status().isOk)
                .andDo(
                    document(
                        "reviews/v1/reviews",
                        preprocessRequest(prettyPrint()),
                        preprocessResponse(prettyPrint()),
                        responseFields(
                            fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                            fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                            fieldWithPath("data.reviews").type(JsonFieldType.ARRAY).description("리뷰 리스트"),
                            fieldWithPath("data.reviews[].reviewId").type(JsonFieldType.NUMBER).description("리뷰 아이디"),
                            fieldWithPath("data.reviews[].content").type(JsonFieldType.STRING).description("리뷰 내용"),
                            fieldWithPath("data.reviews[].score").type(JsonFieldType.NUMBER).description("작성자 상품 리뷰 평점"),
                            fieldWithPath("data.reviews[].email").type(JsonFieldType.STRING).description("리뷰 작성자 이메일"),
                            fieldWithPath("data.reviews[].productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                            fieldWithPath("data.reviews[].updatedAt").type(JsonFieldType.STRING).description("리뷰 작성일"),
                            fieldWithPath("data.reviews[].orderProductId").type(JsonFieldType.NUMBER).optional().description("주문 - 상품 ID"),
                            fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                        ),
                    )
                )
    }

    @Test
    fun addReviewToProduct() {
        given(reviewUseCase.addReviewToProduct(
            AddReviewCommand(
                productId = 1L,
                content = "재미있어요!",
                score = BigDecimal(5),
                email = "abc@naver.com",
                orderProductId = null,
            )
        )).willReturn(1L)

        val request = ReviewCreateRequest(
            productId = 1L,
            content = "재미있어요!",
            score = BigDecimal(5),
            email = "abc@naver.com",
            orderProductId = null,
        )

        mockMvc.perform(
            post("/product/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "reviews/v1/add",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 ID"),
                        fieldWithPath("content").type(JsonFieldType.STRING).description("리뷰 내용"),
                        fieldWithPath("score").type(JsonFieldType.NUMBER).description("리뷰 평점"),
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("orderProductId").type(JsonFieldType.NUMBER).optional().description("주문 - 상푸 리뷰"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("데이터"),
                        fieldWithPath("data.reviewId").type(JsonFieldType.NUMBER).description("생성된 리뷰 ID"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }
}