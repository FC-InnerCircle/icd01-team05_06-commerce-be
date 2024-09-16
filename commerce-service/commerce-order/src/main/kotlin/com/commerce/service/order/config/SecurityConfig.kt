package com.commerce.service.order.config

import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.response.CommonResponse
import com.commerce.common.response.ErrorCode
import com.commerce.common.response.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.logout.LogoutFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher

@Configuration
@EnableWebSecurity
class SecurityConfig(private val env: Environment){

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        memberRepository: MemberRepository,
        objectMapper: ObjectMapper,
        jwtAuthenticationFilter: JwtAuthenticationFilter,
    ): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .logout {
                it.disable()
            }

            if(env.activeProfiles.contains("local")) {
                http
                    .headers {
                        it.frameOptions { frame -> frame.disable() }
                    }
                    .authorizeHttpRequests {
                        it
                            .requestMatchers(
                                AntPathRequestMatcher("/actuator/**"),
                                AntPathRequestMatcher("/docs/**"),
                                AntPathRequestMatcher("/h2-console/**"),
                                AntPathRequestMatcher("/login", HttpMethod.POST.name())
                            ).permitAll()
                            .anyRequest().authenticated()
                    }
            }else {
                http
                    .authorizeHttpRequests {
                        it
                            .requestMatchers(
                                AntPathRequestMatcher("/actuator/**", HttpMethod.GET.name()),
                                AntPathRequestMatcher("/docs/**", HttpMethod.GET.name()),
                            ).permitAll()
                            .anyRequest().authenticated()
                    }
            }
            .exceptionHandling {
                it
                    .authenticationEntryPoint { _, response, _ ->
                        response.status = HttpStatus.FORBIDDEN.value()
                        response.contentType = MediaType.APPLICATION_JSON_VALUE
                        response.characterEncoding = Charsets.UTF_8.name()
                        response.writer.write(objectMapper.writeValueAsString(CommonResponse.error(listOf(ErrorResponse(ErrorCode.PERMISSION_ERROR.code, ErrorCode.PERMISSION_ERROR.message)))))
                    }
                    .accessDeniedHandler { _, response, _ ->
                        response.status = HttpStatus.FORBIDDEN.value()
                        response.contentType = MediaType.APPLICATION_JSON_VALUE
                        response.characterEncoding = Charsets.UTF_8.name()
                        response.writer.write(objectMapper.writeValueAsString(CommonResponse.error(listOf(ErrorResponse(ErrorCode.PERMISSION_ERROR.code, ErrorCode.PERMISSION_ERROR.message)))))
                    }
            }
            .addFilterBefore(jwtAuthenticationFilter, LogoutFilter::class.java)
        return http.build()
    }
}