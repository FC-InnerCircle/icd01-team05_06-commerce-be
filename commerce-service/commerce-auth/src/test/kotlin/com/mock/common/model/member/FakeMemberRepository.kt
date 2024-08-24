package com.mock.common.model.member

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository

class FakeMemberRepository : MemberRepository {

    val data: MutableList<Member> = mutableListOf()

    override fun save(member: Member) {
        data.add(member);
    }

    override fun findByEmail(email: String): Member? {
        return data.find { it.email == email }
    }
}