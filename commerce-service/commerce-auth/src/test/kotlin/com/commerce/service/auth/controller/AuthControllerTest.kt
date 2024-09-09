package com.commerce.service.auth.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginMemberInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginTokenInfoDto
import com.commerce.service.auth.config.SecurityConfig
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
import com.common.RestDocsUtil.Companion.format
import com.fasterxml.jackson.databind.ObjectMapper
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
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*
import org.springframework.restdocs.operation.preprocess.Preprocessors.*
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.restdocs.payload.PayloadDocumentation.*
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
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
        given(
            tokenUseCase.getTokenSubject(
                testAccessToken,
                TokenType.ACCESS_TOKEN
            )
        ).willReturn(testMember.id.toString())
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
            post("/login")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT).description("사용자 정보"),
                        fieldWithPath("data.memberInfo.id").type(JsonFieldType.NUMBER).description("고유번호"),
                        fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("data.memberInfo.phone").type(JsonFieldType.STRING).description("연락처"),
                        fieldWithPath("data.tokenInfo").type(JsonFieldType.OBJECT).description("JWT 토큰 정보"),
                        fieldWithPath("data.tokenInfo.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("data.tokenInfo.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
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
            post("/sign-up")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun logout() {
        mockMvc.perform(
            post("/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "logout",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
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
            post("/refresh")
                .header("refresh-token", "Bearer $refreshToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "refresh",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
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
            put("/update")
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
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.memberInfo").type(JsonFieldType.OBJECT).description("사용자 정보"),
                        fieldWithPath("data.memberInfo.id").type(JsonFieldType.NUMBER).description("고유번호"),
                        fieldWithPath("data.memberInfo.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.memberInfo.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("data.memberInfo.phone").type(JsonFieldType.STRING).description("연락처"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun withdrawal() {
        mockMvc.perform(
            delete("/withdrawal")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "withdrawal",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).optional().description("응답 데이터"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }
}