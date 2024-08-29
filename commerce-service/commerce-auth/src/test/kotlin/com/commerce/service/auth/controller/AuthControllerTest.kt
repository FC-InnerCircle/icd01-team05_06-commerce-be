package com.commerce.service.auth.controller

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.auth.application.service.TokenType
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.TokenUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginMemberInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginTokenInfoDto
import com.commerce.service.auth.config.JwtAuthenticationFilter
import com.commerce.service.auth.config.SecurityConfig
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import com.common.RestDocsUtil.Companion.format
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.http.Cookie
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
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
        given(tokenUseCase.getTokenSubject(testAccessToken, TokenType.ACCESS_TOKEN)).willReturn(testMember.id.toString())
        given(memberRepository.findById(testMember.id)).willReturn(testMember)
    }

    @Test
    fun login() {
        val request = LoginRequest(
            email = "commerce@example.com",
            password = "123!@#qwe",
        )

        given(authUseCase.login(request.toCommand())).willReturn(
            LoginInfoDto(
                memberInfo = LoginMemberInfoDto(
                    id = 1,
                    email = "commerce@example.com",
                    name = "홍길동",
                    phone = "01012345678"
                ), tokenInfo = LoginTokenInfoDto(
                    accessToken = "accessToken",
                    refreshToken = "refreshToken"
                )
            )
        )

        mockMvc.perform(
            post("/login").with(SpaCsrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "login",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("memberInfo").type(JsonFieldType.OBJECT).description("사용자 정보"),
                        fieldWithPath("memberInfo.id").type(JsonFieldType.NUMBER).description("고유번호"),
                        fieldWithPath("memberInfo.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("memberInfo.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("memberInfo.phone").type(JsonFieldType.STRING).description("연락처"),
                        fieldWithPath("tokenInfo").type(JsonFieldType.OBJECT).description("JWT 토큰 정보"),
                        fieldWithPath("tokenInfo.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("tokenInfo.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                    )
                )
            )
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

    @Test
    fun logout() {
        mockMvc.perform(
            post("/logout").with(SpaCsrf())
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "logout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("message").type(JsonFieldType.STRING).description("결과 메세지"),
                    )
                )
            )
    }

    @Test
    fun refresh() {
        val refreshToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c"
        given(authUseCase.refresh(refreshToken)).willReturn(testAccessToken)

        mockMvc.perform(
            post("/refresh").with(SpaCsrf())
                .header("refresh-token", "Bearer $refreshToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "refresh",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                    )
                )
            )
    }

    @Test
    fun update() {
        val request = UpdateRequest(
            password = "123!@#qwe",
            name = "홍길동",
            phone = "01012345678"
        )

        given(authUseCase.update(testMember, request.toCommand())).willReturn(
            LoginMemberInfoDto(
                id = 1,
                email = "commerce@example.com",
                name = "홍길동",
                phone = "01012345678"
            )
        )

        mockMvc.perform(
            put("/update").with(SpaCsrf())
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                            .attributes(format("숫자만 입력")),
                    ),
                    responseFields(
                        fieldWithPath("memberInfo").type(JsonFieldType.OBJECT).description("사용자 정보"),
                        fieldWithPath("memberInfo.id").type(JsonFieldType.NUMBER).description("고유번호"),
                        fieldWithPath("memberInfo.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("memberInfo.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("memberInfo.phone").type(JsonFieldType.STRING).description("연락처"),
                    )
                )
            )
    }

    @Test
    fun withdrawal() {
        mockMvc.perform(
            delete("/withdrawal").with(SpaCsrf())
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "withdrawal",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
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
