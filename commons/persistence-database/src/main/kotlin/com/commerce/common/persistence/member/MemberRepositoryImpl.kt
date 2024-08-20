package com.commerce.common.persistence.member

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val memberJpaRepository: MemberJpaRepository
) : MemberRepository {

    override fun findById(id: Long): Member? {

        val optionalMember = memberJpaRepository.findById(id)
        if (optionalMember.isPresent) {
            return Member(
                id = optionalMember.get().id,
                name = optionalMember.get().name
            )
        }
        return null
    }
}