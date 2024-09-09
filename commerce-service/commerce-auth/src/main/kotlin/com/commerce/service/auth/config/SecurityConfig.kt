package com.commerce.service.auth.config

import com.commerce.common.jwt.config.JwtAuthenticationFilter
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.service.auth.controller.common.BaseResponse
import com.commerce.service.auth.controller.common.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
class SecurityConfig {

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
                it.logoutSuccessHandler { _, response, authentication ->
                    val member = authentication?.principal as? Member
                    if (member != null) {
                        memberRepository.save(member.logout())
                    }

                    response.contentType = MediaType.APPLICATION_JSON_VALUE
                    response.characterEncoding = Charsets.UTF_8.name()
                    response.writer.write(objectMapper.writeValueAsString(BaseResponse.success()))
                }
            }
            .authorizeHttpRequests {
                it
                    .requestMatchers(
                        AntPathRequestMatcher("/actuator/**", HttpMethod.GET.name()),
                        AntPathRequestMatcher("/docs/**", HttpMethod.GET.name()),
                        AntPathRequestMatcher("/login", HttpMethod.POST.name()),
                        AntPathRequestMatcher("/sign-up", HttpMethod.POST.name()),
                        AntPathRequestMatcher("/refresh", HttpMethod.POST.name()),
                    ).permitAll()
                    .anyRequest().authenticated()
            }
            .exceptionHandling {
                it
                    .authenticationEntryPoint { _, response, _ ->
                        response.status = HttpStatus.FORBIDDEN.value()
                        response.contentType = MediaType.APPLICATION_JSON_VALUE
                        response.characterEncoding = Charsets.UTF_8.name()
                        response.writer.write(objectMapper.writeValueAsString(ErrorResponse("권한이 없습니다.")))
                    }
                    .accessDeniedHandler { _, response, _ ->
                        response.status = HttpStatus.FORBIDDEN.value()
                        response.contentType = MediaType.APPLICATION_JSON_VALUE
                        response.characterEncoding = Charsets.UTF_8.name()
                        response.writer.write(objectMapper.writeValueAsString(ErrorResponse("권한이 없습니다.")))
                    }
            }
            .addFilterBefore(jwtAuthenticationFilter, LogoutFilter::class.java)
        return http.build()
    }
}