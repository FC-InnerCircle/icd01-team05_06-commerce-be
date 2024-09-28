package com.commerce.common.persistence.member

import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, Long> {

    fun findByEmail(email: String): MemberJpaEntity?
}