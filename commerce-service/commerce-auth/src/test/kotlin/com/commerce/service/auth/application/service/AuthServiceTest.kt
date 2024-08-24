package com.commerce.service.auth.application.service

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.mock.common.model.member.FakeMemberRepository
import com.mock.config.FakePasswordEncoder
import org.assertj.core.api.AssertionsForClassTypes.assertThat
import org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.password.PasswordEncoder

class AuthServiceTest {

    private lateinit var memberRepository: FakeMemberRepository
    private lateinit var passwordEncoder: PasswordEncoder
    private val authService by lazy {
        AuthService(memberRepository, passwordEncoder)
    };

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        passwordEncoder = FakePasswordEncoder()
    }

    @Test
    fun `사용자를 저장할 수 있다`() {
        val command = SignUpCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        )

        authService.signUp(command)

        val member = memberRepository.findByEmail("jerome.boyd@example.com")
        assertThat(member).isNotNull()
        assertThat(member!!.email).isEqualTo("jerome.boyd@example.com")
        assertThat(member.name).isEqualTo("Cameron Mayo")
        assertThat(member.phone).isEqualTo("(737) 231-4205")
    }

    @Test
    fun `중복된 이메일은 가입이 되지 않는다`() {
        memberRepository.data.add(
            Member(
                email = "jerome.boyd@example.com",
                password = "viverra",
                name = "Krista Wall",
                phone = "(352) 893-0816"
            )
        )

        val command = SignUpCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        )

        assertThatThrownBy {
            authService.signUp(command)
        }.isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    fun `비밀번호를 암호화하여 저장할 수 있다`() {
        val command = SignUpCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        )

        authService.signUp(command)

        val member = memberRepository.findByEmail("jerome.boyd@example.com")
        assertThat(passwordEncoder.matches("phasellus", member!!.password)).isTrue()
    }
}