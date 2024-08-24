package com.commerce.common.persistence.member

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun save(member: Member): Member {
        return memberJpaRepository.save(MemberJpaEntity.from(member)).toModel()
    }

    override fun findByEmail(email: String): Member? {
        return memberJpaRepository.findByEmail(email)
    }

    override fun findById(id: Long): Member? {
        return memberJpaRepository.findByIdOrNull(id)?.toModel()
    }
}