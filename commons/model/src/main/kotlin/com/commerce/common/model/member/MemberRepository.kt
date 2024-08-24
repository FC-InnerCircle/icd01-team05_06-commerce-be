package com.commerce.common.model.member

interface MemberRepository {

    fun save(member: Member)
    fun findByEmail(email: String): Member?
}