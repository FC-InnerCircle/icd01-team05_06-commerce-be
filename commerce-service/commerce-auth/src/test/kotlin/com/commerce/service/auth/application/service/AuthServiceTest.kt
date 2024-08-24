package com.commerce.service.auth.application.service

import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.command.SignInCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.exception.AuthException
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
    fun `저장된 아이디가 없으면 로그인에 실패한다`() {
        val command = SignInCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        assertThatThrownBy {
            authService.login(command)
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("아이디 혹은 비밀번호가 일치하지 않습니다.")
    }

    @Test
    fun `비밀번호가 틀리면 로그인에 실패한다`() {
        memberRepository.save(Member(
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))
        val command = SignInCommand(
            email = "jerome.boyd@example.com",
            password = "safsafwa",
        )

        assertThatThrownBy {
            authService.login(command)
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("아이디 혹은 비밀번호가 일치하지 않습니다.")
    }

    @Test
    fun `로그인에 성공하면 마지막 로그인 시간을 저장한다`() {
        memberRepository.save(Member(
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))
        val command = SignInCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        authService.login(command)

        val member = memberRepository.findByEmail("jerome.boyd@example.com")!!
        assertThat(member.lastLoginDate).isNotNull()
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
        memberRepository.save(
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
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("중복된 이메일입니다.")
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