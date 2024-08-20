package com.commerce.service.auth.application.service

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import com.commerce.service.auth.application.usecase.AuthUseCase
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val memberRepository: MemberRepository
) : AuthUseCase {

    override fun findMember(id: Long): Member? {
        return memberRepository.findById(id)
    }
}