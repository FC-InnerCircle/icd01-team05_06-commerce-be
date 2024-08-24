package com.mock.common.model.member

import com.commerce.common.model.member.Member
import com.commerce.common.model.member.MemberRepository

class FakeMemberRepository : MemberRepository {

    var autoIncrementId = 1L
    var data: MutableList<Member> = mutableListOf()

    override fun save(member: Member) {
        if (member.id > 0) {
            data = data.filter { it.id != member.id }.toMutableList()
            data.add(member)
        } else {
            data.add(
                Member(
                    id = autoIncrementId++,
                    email = member.email,
                    password = member.password,
                    name = member.name,
                    phone = member.phone,
                    lastLoginDate = member.lastLoginDate
                )
            )
        }
    }

    override fun findByEmail(email: String): Member? {
        return data.find { it.email == email }
    }
}