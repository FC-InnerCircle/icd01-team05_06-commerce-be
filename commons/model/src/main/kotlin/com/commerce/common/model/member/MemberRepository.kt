package com.commerce.common.model.member

interface MemberRepository {

    fun save(member: Member): Member
    fun findByEmail(email: String): Member?
    fun findById(id: Long): Member?
}