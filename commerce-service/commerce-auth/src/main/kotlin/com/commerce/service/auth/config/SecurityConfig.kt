package com.commerce.service.auth.config

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.service.auth.controller.common.BaseResponse
import com.commerce.service.auth.controller.common.ErrorResponse
import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import org.springframework.security.web.csrf.*
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.util.function.Supplier

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
                it.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                it.csrfTokenRequestHandler(SpaCsrfTokenRequestHandler())
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
            .addFilterAfter(CsrfCookieFilter(), BasicAuthenticationFilter::class.java)
        return http.build()
    }
}

class SpaCsrfTokenRequestHandler : CsrfTokenRequestAttributeHandler() {
    private val delegate: CsrfTokenRequestHandler = XorCsrfTokenRequestAttributeHandler()

    override fun handle(request: HttpServletRequest, response: HttpServletResponse, csrfToken: Supplier<CsrfToken>) {
        /*
         * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
         * the CsrfToken when it is rendered in the response body.
         */
        delegate.handle(request, response, csrfToken)
    }

    override fun resolveCsrfTokenValue(request: HttpServletRequest, csrfToken: CsrfToken): String? {
        /*
         * If the request contains a request header, use CsrfTokenRequestAttributeHandler
         * to resolve the CsrfToken. This applies when a single-page application includes
         * the header value automatically, which was obtained via a cookie containing the
         * raw CsrfToken.
         */
        return if (StringUtils.hasText(request.getHeader(csrfToken.headerName))) {
            super.resolveCsrfTokenValue(request, csrfToken)
        } else {
            /*
             * In all other cases (e.g. if the request contains a request parameter), use
             * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
             * when a server-side rendered form includes the _csrf request parameter as a
             * hidden input.
             */
            delegate.resolveCsrfTokenValue(request, csrfToken)
        }
    }
}

class CsrfCookieFilter : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val csrfToken = request.getAttribute("_csrf") as CsrfToken
        // Render the token value to a cookie by causing the deferred token to be loaded
        csrfToken.token
        filterChain.doFilter(request, response)
    }
}