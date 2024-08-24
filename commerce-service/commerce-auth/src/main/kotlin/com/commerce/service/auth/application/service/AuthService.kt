package com.commerce.service.auth.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.TokenUseCase
import com.commerce.service.auth.application.usecase.command.SignInCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginMemberInfoDto
import com.commerce.service.auth.application.usecase.dto.LoginTokenInfoDto
import com.commerce.service.auth.application.usecase.exception.AuthException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenUseCase: TokenUseCase
) : AuthUseCase {

    @Transactional
    override fun login(command: SignInCommand): LoginInfoDto {
        val loginFailedMessage = "아이디 혹은 비밀번호가 일치하지 않습니다."

        val member = memberRepository.findByEmail(command.email) ?: throw AuthException(loginFailedMessage)
        if (!passwordEncoder.matches(command.password, member.password)) throw AuthException(loginFailedMessage)

        val accessToken = tokenUseCase.createToken(member.id, TokenType.ACCESS_TOKEN)
        val refreshToken = tokenUseCase.createToken(member.id, TokenType.REFRESH_TOKEN)

        val newMember = memberRepository.save(member.login(refreshToken))

        return LoginInfoDto(
            memberInfo = LoginMemberInfoDto(
                id = newMember.id,
                email = newMember.email,
                name = member.name,
                phone = member.phone
            ),
            tokenInfo = LoginTokenInfoDto(
                accessToken = accessToken,
                refreshToken = refreshToken
            )
        )
    }

    @Transactional
    override fun signUp(command: SignUpCommand) {
        memberRepository.findByEmail(command.email)?.let {
            throw AuthException("중복된 이메일입니다.")
        }

        memberRepository.save(
            Member(
                email = command.email,
                password = passwordEncoder.encode(command.password),
                name = command.name,
                phone = command.phone
            )
        )
    }
}