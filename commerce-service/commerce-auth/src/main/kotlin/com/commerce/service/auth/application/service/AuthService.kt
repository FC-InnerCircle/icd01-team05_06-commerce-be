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
    override fun login(command: LoginCommand): LoginInfoDto {
        val member = memberRepository.findByEmail(command.email) ?: throw AuthException(ErrorCode.LOGIN_FAILED)
        if (!passwordEncoder.matches(command.password, member.password)) throw AuthException(ErrorCode.LOGIN_FAILED)

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

    override fun refresh(refreshToken: String): String {
        val id = (tokenUseCase.getTokenSubject(refreshToken, TokenType.REFRESH_TOKEN)
            ?: throw AuthException(ErrorCode.PERMISSION_ERROR))
            .toLong()

        memberRepository.findById(id)?.let {
            if (it.refreshToken != refreshToken) {
                throw AuthException(ErrorCode.PERMISSION_ERROR)
            }
        } ?: throw AuthException(ErrorCode.PERMISSION_ERROR)

        return tokenUseCase.createToken(id, TokenType.ACCESS_TOKEN)
    }

    override fun update(member: Member, command: UpdateCommand): LoginMemberInfoDto {
        val newMember = memberRepository.save(
            member.update(
                password = command.password?.let { passwordEncoder.encode(it) },
                name = command.name,
                phone = command.phone,
            )
        )
        return LoginMemberInfoDto(
            id = newMember.id,
            email = newMember.email,
            name = newMember.name,
            phone = newMember.phone,
        )
    }

    override fun withdrawal(member: Member) {
        memberRepository.deleteById(member.id)
    }
}