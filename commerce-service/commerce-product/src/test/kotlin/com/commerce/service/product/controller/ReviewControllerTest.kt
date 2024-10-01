package com.commerce.service.product.controller

import com.commerce.common.model.member.MemberRepository
import com.commerce.common.model.review.ReviewRepository
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.product.application.usecase.ReviewUseCase
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

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

}