package com.commerce.service.auth.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.service.auth.application.usecase.AuthUseCase
import com.commerce.service.auth.application.usecase.command.SignUpCommand
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AuthService(
    private val memberRepository: MemberRepository
) : AuthUseCase {

    @Transactional
    override fun signUp(command: SignUpCommand) {
        memberRepository.save(Member(
            email = command.email,
            password = command.password,
            name = command.name,
            phone = command.phone
        ))
    }
}