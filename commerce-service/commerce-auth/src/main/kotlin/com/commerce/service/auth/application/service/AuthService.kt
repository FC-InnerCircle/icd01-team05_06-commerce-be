package com.commerce.service.auth.application.service

import com.commerce.common.jwt.application.service.TokenType
import com.commerce.common.jwt.application.usecase.TokenUseCase
import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.common.response.ErrorCode
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.command.LoginCommand
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import com.commerce.service.auth.application.usecase.command.UpdateCommand
import com.commerce.service.auth.application.usecase.dto.LoginInfoDto
import com.commerce.service.auth.application.usecase.dto.TokenInfoDto
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
    override fun login(command: LoginCommand): LoginInfoDto {
        val member = memberRepository.findByEmail(command.email) ?: throw AuthException(ErrorCode.LOGIN_FAILED)
        if (!passwordEncoder.matches(command.password, member.password)) throw AuthException(ErrorCode.LOGIN_FAILED)

        val accessTokenDto = tokenUseCase.createToken(member.id, TokenType.ACCESS_TOKEN)
        val refreshTokenDto = tokenUseCase.createToken(member.id, TokenType.REFRESH_TOKEN)

        memberRepository.save(member.login(refreshTokenDto.token))

        return LoginInfoDto(
            tokenInfo = TokenInfoDto(
                accessToken = accessTokenDto.token,
                accessTokenExpiresIn = accessTokenDto.expiresIn,
                refreshToken = refreshTokenDto.token,
                refreshTokenExpiresIn = refreshTokenDto.expiresIn
            )
        )
    }

    @Transactional
    override fun signUp(command: SignUpCommand) {
        memberRepository.findByEmail(command.email)?.let {
            throw AuthException(ErrorCode.DUPLICATED_EMAIL)
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

    override fun refresh(refreshToken: String): LoginInfoDto {
        val id = (tokenUseCase.getTokenSubject(refreshToken, TokenType.REFRESH_TOKEN)
            ?: throw AuthException(ErrorCode.PERMISSION_ERROR))
            .toLong()

        memberRepository.findById(id)?.let {
            if (it.refreshToken != refreshToken) {
                throw AuthException(ErrorCode.PERMISSION_ERROR)
            }
        } ?: throw AuthException(ErrorCode.PERMISSION_ERROR)

        val accessTokenDto = tokenUseCase.createToken(id, TokenType.ACCESS_TOKEN)
        // TODO : kakao처럼 기존 refresh token이 7일 이하로 남은 경우만 갱신하도록 하기?
        val refreshTokenDto = tokenUseCase.createToken(id, TokenType.REFRESH_TOKEN)

        return LoginInfoDto(
            tokenInfo = TokenInfoDto(
                accessToken = accessTokenDto.token,
                accessTokenExpiresIn = accessTokenDto.expiresIn,
                refreshToken = refreshTokenDto.token,
                refreshTokenExpiresIn = refreshTokenDto.expiresIn
            )
        )
    }

    override fun update(member: Member, command: UpdateCommand) {
        memberRepository.save(
            member.update(
                password = command.password?.let { passwordEncoder.encode(it) },
                name = command.name,
                phone = command.phone,
            )
        )
    }

    override fun withdrawal(member: Member) {
        memberRepository.deleteById(member.id)
    }
}