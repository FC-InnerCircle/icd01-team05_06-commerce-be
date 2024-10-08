package com.commerce.service.auth.controller

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.address.Address
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.restdocs.RestDocsUtil.Companion.format
import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.TokenInfoDto
import com.commerce.service.auth.config.SecurityConfig
import com.commerce.service.auth.controller.request.LoginRequest
import com.commerce.service.auth.controller.request.PasswordVerifyRequest
import com.commerce.service.auth.controller.request.SignUpRequest
import com.commerce.service.auth.controller.request.UpdateRequest
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
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Import(AuthController::class, ObjectMapperConfig::class, JwtAuthenticationFilter::class)
@ExtendWith(RestDocumentationExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@WebMvcTest(controllers = [AuthController::class])
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
        phone = "01012345678",
        address = Address(
            postalCode = "12345",
            streetAddress = "서울 종로구 테스트동",
            detailAddress = "123-45"
        )
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
                tokenInfo = TokenInfoDto(
                    accessToken = "accessToken",
                    accessTokenExpiresIn = System.currentTimeMillis() + TokenType.ACCESS_TOKEN.validTime.inWholeMilliseconds,
                    refreshToken = "refreshToken",
                    refreshTokenExpiresIn = System.currentTimeMillis() + TokenType.REFRESH_TOKEN.validTime.inWholeMilliseconds,
                )
            )
        )

        mockMvc.perform(
            post("/auth/v1/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/login",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.tokenInfo").type(JsonFieldType.OBJECT).description("JWT 토큰 정보"),
                        fieldWithPath("data.tokenInfo.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("data.tokenInfo.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                            .description("액세스 토큰 만료 시각"),
                        fieldWithPath("data.tokenInfo.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                        fieldWithPath("data.tokenInfo.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                            .description("리프레쉬 토큰 만료 시각"),
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
            phone = "01012345678",
            postalCode = "12345",
            streetAddress = "서울 종로구 테스트동",
            detailAddress = "123-45"
        )

        mockMvc.perform(
            post("/auth/v1/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/sign-up",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일 (아이디)"),
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                            .attributes(format("숫자만 입력")),
                        fieldWithPath("postalCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("streetAddress").type(JsonFieldType.STRING).description("지번주소"),
                        fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세주소")
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
    fun info() {
        mockMvc.perform(
            get("/auth/v1/info")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/info",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.id").type(JsonFieldType.NUMBER).description("고유번호"),
                        fieldWithPath("data.email").type(JsonFieldType.STRING).description("이메일"),
                        fieldWithPath("data.name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("data.phone").type(JsonFieldType.STRING).description("연락처"),
                        fieldWithPath("data.postalCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("data.streetAddress").type(JsonFieldType.STRING).description("지번주소"),
                        fieldWithPath("data.detailAddress").type(JsonFieldType.STRING).description("상세주소"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }


    @Test
    fun logout() {
        mockMvc.perform(
            post("/auth/v1/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/logout",
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
        given(authUseCase.refresh(refreshToken)).willReturn(
            LoginInfoDto(
                TokenInfoDto(
                    accessToken = testAccessToken,
                    accessTokenExpiresIn = System.currentTimeMillis() + TokenType.ACCESS_TOKEN.validTime.inWholeMilliseconds,
                    refreshToken = refreshToken,
                    refreshTokenExpiresIn = System.currentTimeMillis() + TokenType.REFRESH_TOKEN.validTime.inWholeMilliseconds,
                )
            )
        )

        mockMvc.perform(
            post("/auth/v1/refresh")
                .header("refresh-token", "Bearer $refreshToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/refresh",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.tokenInfo").type(JsonFieldType.OBJECT).description("JWT 토큰 정보"),
                        fieldWithPath("data.tokenInfo.accessToken").type(JsonFieldType.STRING).description("액세스 토큰"),
                        fieldWithPath("data.tokenInfo.accessTokenExpiresIn").type(JsonFieldType.NUMBER)
                            .description("액세스 토큰 만료 시각"),
                        fieldWithPath("data.tokenInfo.refreshToken").type(JsonFieldType.STRING).description("리프레쉬 토큰"),
                        fieldWithPath("data.tokenInfo.refreshTokenExpiresIn").type(JsonFieldType.NUMBER)
                            .description("리프레쉬 토큰 만료 시각"),
                        fieldWithPath("error").type(JsonFieldType.ARRAY).optional().description("오류 정보")
                    )
                )
            )
    }

    @Test
    fun passwordVerify() {
        val request = PasswordVerifyRequest(
            password = "123!@#qwe",
        )
        given(authUseCase.passwordVerify(testMember, "123!@#qwe")).willReturn("test-auth-token")

        mockMvc.perform(
            post("/auth/v1/password-verify")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/password-verify",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                    ),
                    responseFields(
                        fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("요청 성공 여부"),
                        fieldWithPath("data").type(JsonFieldType.OBJECT).description("응답 데이터"),
                        fieldWithPath("data.token").type(JsonFieldType.STRING).description("인증 토큰"),
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
            phone = "01012345678",
            postalCode = "12345",
            streetAddress = "서울 종로구 테스트동",
            detailAddress = "123-45"
        )

        mockMvc.perform(
            put("/auth/v1/update")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
                .header("auth-token", "test-auth-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/update",
                    preprocessRequest(prettyPrint()),
                    preprocessResponse(prettyPrint()),
                    requestFields(
                        fieldWithPath("password").type(JsonFieldType.STRING).optional()
                            .description("비밀번호 (없는 경우 기존 유지)"),
                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                        fieldWithPath("phone").type(JsonFieldType.STRING).description("연락처")
                            .attributes(format("숫자만 입력")),
                        fieldWithPath("postalCode").type(JsonFieldType.STRING).description("우편번호"),
                        fieldWithPath("streetAddress").type(JsonFieldType.STRING).description("지번주소"),
                        fieldWithPath("detailAddress").type(JsonFieldType.STRING).description("상세주소"),
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
    fun withdrawal() {
        mockMvc.perform(
            delete("/auth/v1/withdrawal")
                .header(HttpHeaders.AUTHORIZATION, "Bearer $testAccessToken")
        )
            .andExpect(status().isOk)
            .andDo(
                document(
                    "auth/v1/withdrawal",
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