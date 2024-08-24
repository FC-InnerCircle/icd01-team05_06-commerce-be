package com.commerce.service.auth.controller

import com.commerce.common.util.ObjectMapperConfig
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.config.SecurityConfig
import com.commerce.service.auth.controller.request.SignUpRequest
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

@Import(ObjectMapperConfig::class)
@ExtendWith(MockitoExtension::class)
@ContextConfiguration(classes = [SecurityConfig::class])
@WebMvcTest(controllers = [AuthController::class])
@ComponentScan(basePackages = ["com.commerce.service.auth.controller"])
class AuthControllerTest(
    @Autowired
    private val objectMapper: ObjectMapper
) {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var authUseCase: AuthUseCase

    @BeforeEach
    fun setUp(applicationContext: WebApplicationContext) {
        mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .apply<DefaultMockMvcBuilder>(springSecurity())
            .build()
    }

    @Test
    fun signUp() {
        val request = SignUpRequest(
            email = "carroll.mitchell@example.com",
            password = "evertitur",
            name = "Ernesto Poole",
            phone = "(263) 199-4774"
        )

        mockMvc.perform(
            post("/sign-up").with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
    }
}