package com.commerce.common.persistence.member

import com.commerce.common.model.member.Member
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table
class MemberJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var email: String,

    @Column(nullable = false)
    var password: String,

    @Column(nullable = false)
    var phone: String,

    @Column
    var lastLoginDate: LocalDateTime?,
) {
    companion object {
        fun from(member: Member) = MemberJpaEntity(
            id = member.id,
            name = member.name,
            email = member.email,
            password = member.password,
            phone = member.phone,
            lastLoginDate = member.lastLoginDate
        )
    }
}