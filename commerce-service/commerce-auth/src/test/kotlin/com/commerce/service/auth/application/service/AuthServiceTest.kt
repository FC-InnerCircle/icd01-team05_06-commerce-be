package com.commerce.service.auth.application.service

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.model.member.Member
import com.commerce.service.auth.application.usecase.command.LoginCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.command.UpdateCommand
import com.commerce.service.auth.application.usecase.exception.AuthException
import com.mock.common.jwt.application.usecase.FakeTokenUseCase
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
    private lateinit var tokenUseCase: TokenUseCase
    private val authService by lazy {
        AuthService(memberRepository, passwordEncoder, tokenUseCase)
    };

    @BeforeEach
    fun setUp() {
        memberRepository = FakeMemberRepository()
        passwordEncoder = FakePasswordEncoder()
        tokenUseCase = FakeTokenUseCase()
    }

    @Test
    fun `저장된 아이디가 없으면 로그인에 실패한다`() {
        val command = LoginCommand(
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
        val command = LoginCommand(
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
        val command = LoginCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        authService.login(command)

        val member = memberRepository.findByEmail("jerome.boyd@example.com")!!
        assertThat(member.lastLoginDate).isNotNull()
    }

    @Test
    fun `로그인에 성공하면 리프레쉬 토큰을 저장한다`() {
        memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))
        val command = LoginCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        authService.login(command)

        val member = memberRepository.findByEmail("jerome.boyd@example.com")!!
        assertThat(member.refreshToken).isEqualTo(tokenUseCase.createToken(1, TokenType.REFRESH_TOKEN))
    }

    @Test
    fun `로그인에 성공하면 필요한 사용자 정보를 반환한다`() {
        memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))
        val command = LoginCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        val result = authService.login(command)

        assertThat(result.memberInfo.id).isEqualTo(1)
        assertThat(result.memberInfo.email).isEqualTo("jerome.boyd@example.com")
        assertThat(result.memberInfo.name).isEqualTo("Cameron Mayo")
        assertThat(result.memberInfo.phone).isEqualTo("(737) 231-4205")
    }

    @Test
    fun `로그인에 성공하면 토큰을 생성하여 반환한다`() {
        memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))
        val command = LoginCommand(
            email = "jerome.boyd@example.com",
            password = "phasellus",
        )

        val result = authService.login(command)

        assertThat(result.tokenInfo.accessToken).isEqualTo(tokenUseCase.createToken(1, TokenType.ACCESS_TOKEN))
        assertThat(result.tokenInfo.refreshToken).isEqualTo(tokenUseCase.createToken(1, TokenType.REFRESH_TOKEN))
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

    @Test
    fun `토큰이 만료된 경우 재로그인 필요 에러를 던진다`() {
        val refreshToken = "invalid token"

        assertThatThrownBy {
            authService.refresh(refreshToken)
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("Permission Denied")
    }

    @Test
    fun `토큰이 정상이지만 Member가 없으면 재로그인 필요 에러를 던진다`() {
        val refreshToken = tokenUseCase.createToken(1, TokenType.REFRESH_TOKEN)

        assertThatThrownBy {
            authService.refresh(refreshToken)
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("Permission Denied")
    }

    @Test
    fun `토큰이 정상이지만 Member 테이블 내 컬럼과 값이 다르면 재로그인 필요 에러를 던진다`() {
        val refreshToken = tokenUseCase.createToken(1, TokenType.REFRESH_TOKEN)
        memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = "viverra",
            name = "Krista Wall",
            phone = "(352) 893-0816",
            refreshToken = "other token"
        ))

        assertThatThrownBy {
            authService.refresh(refreshToken)
        }
            .isInstanceOf(AuthException::class.java)
            .hasMessage("Permission Denied")
    }

    @Test
    fun `토큰이 정상이고 Member 테이블 내 컬럼과 값이 일치하면 재발급한다`() {
        val refreshToken = tokenUseCase.createToken(1, TokenType.REFRESH_TOKEN)
        memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = "viverra",
            name = "Krista Wall",
            phone = "(352) 893-0816",
            refreshToken = refreshToken
        ))

        val result = authService.refresh(refreshToken)

        assertThat(tokenUseCase.getTokenSubject(result, TokenType.ACCESS_TOKEN)).isEqualTo("1")
    }

    @Test
    fun `정보 수정 시 비밀번호를 입력하지 않으면 회원의 기본 정보만 변경된다`() {
        val member = memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))

        val command = UpdateCommand(
            password = null,
            name = "After Name",
            phone = "123-4567"
        )

        authService.update(member, command)

        val newMember = memberRepository.findById(1)!!
        assertThat(newMember.password).isEqualTo(passwordEncoder.encode("phasellus"))
        assertThat(newMember.name).isEqualTo("After Name")
        assertThat(newMember.phone).isEqualTo("123-4567")
    }

    @Test
    fun `정보 수정 시 비밀번호를 입력하면 회원의 비밀번호와 기본 정보가 변경된다`() {
        val member = memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = passwordEncoder.encode("phasellus"),
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))

        val command = UpdateCommand(
            password = "After Password",
            name = "After Name",
            phone = "123-4567"
        )

        authService.update(member, command)

        val newMember = memberRepository.findById(1)!!
        assertThat(newMember.password).isEqualTo(passwordEncoder.encode("After Password"))
        assertThat(newMember.name).isEqualTo("After Name")
        assertThat(newMember.phone).isEqualTo("123-4567")
    }

    @Test
    fun `회원탈퇴를 하면 다시 회원 조회를 할 수 없다`() {
        val member = memberRepository.save(Member(
            id = 1,
            email = "jerome.boyd@example.com",
            password = "phasellus",
            name = "Cameron Mayo",
            phone = "(737) 231-4205"
        ))

        authService.withdrawal(member)

        assertThat(memberRepository.findById(1)).isNull()
    }
}