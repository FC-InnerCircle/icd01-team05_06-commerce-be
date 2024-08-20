package com.commerce.common.persistence.member

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun save(member: Member) {
        memberJpaRepository.save(MemberJpaEntity.from(member))
    }
}