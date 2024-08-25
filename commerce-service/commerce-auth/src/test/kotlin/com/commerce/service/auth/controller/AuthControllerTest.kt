package com.commerce.service.auth.controller

import com.commerce.common.model.member.MemberRepository
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.TokenUseCase
import com.commerce.service.auth.config.JwtAuthenticationFilter
import com.commerce.service.auth.config.SecurityConfig
import com.commerce.service.auth.controller.request.SignUpRequest
import com.common.RestDocsUtil.Companion.format
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.security.web.csrf.CookieCsrfTokenRepository
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.RequestPostProcessor
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Import(ObjectMapperConfig::class, JwtAuthenticationFilter::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@WebMvcTest(controllers = [AuthController::class])
@ComponentScan(basePackages = ["com.commerce.service.auth.controller"])
class AuthControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper,
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var tokenUseCase: TokenUseCase

    @MockBean
    private lateinit var memberRepository: MemberRepository

    @MockBean
    private lateinit var authUseCase: AuthUseCase

    @BeforeEach
    fun setUp(
        applicationContext: WebApplicationContext,
        restDocumentation: RestDocumentationContextProvider
    ) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .apply<DefaultMockMvcBuilder>(documentationConfiguration(restDocumentation))
            .build()
    }

    @Test
    fun signUp() {
        val request = SignUpRequest(
            email = "commerce@example.com",
            password = "123!@#qwe",
            name = "홍길동",
            phone = "01012345678"
        )

        mockMvc.perform(
            post("/sign-up").with(SpaCsrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "sign-up",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 (아이디)"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                            .attributes(format("숫자만 입력")),
                    ),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                    )
                )
            )
    }
}

class SpaCsrf : RequestPostProcessor {

    private val cookieCsrfTokenRepository = CookieCsrfTokenRepository()

    override fun postProcessRequest(request: MockHttpServletRequest): MockHttpServletRequest {
        val csrfToken = cookieCsrfTokenRepository.generateToken(request)
        val cookie = Cookie("XSRF-TOKEN", csrfToken.token)
        request.setCookies(*request.cookies?.plus(cookie) ?: arrayOf(cookie))
        request.addHeader(csrfToken.headerName, csrfToken.token)
        return request
    }
}
