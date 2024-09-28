package com.commerce.common.persistence.member

import com.commerce.common.model.member.Member
import com.commerce.common.persistence.BaseTimeEntity
import com.commerce.common.persistence.address.AddressEmbeddable
import jakarta.persistence.*
import org.hibernate.annotations.SQLDelete
import org.hibernate.annotations.SQLRestriction
import java.time.LocalDateTime

@Entity
@Table(name = "member")
@SQLRestriction("deleted_at is NULL")
@SQLDelete(sql = "UPDATE member SET deleted_at = NOW() WHERE id = ?")
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

    @Embedded
    var address: AddressEmbeddable,

    @Column
    var lastLoginDate: LocalDateTime?,

    @Column
    var refreshToken: String?,

    @Column
    var deletedAt: LocalDateTime? = null,
) : BaseTimeEntity() {
    fun toModel(): Member = Member(
        id = id,
        email = email,
        password = password,
        name = name,
        phone = phone,
        address = address.toModel(),
        lastLoginDate = lastLoginDate,
        refreshToken = refreshToken,
    )

    companion object {
        fun from(member: Member) = MemberJpaEntity(
            id = member.id,
            name = member.name,
            email = member.email,
            password = member.password,
            phone = member.phone,
            address = AddressEmbeddable.from(member.address),
            lastLoginDate = member.lastLoginDate,
            refreshToken = member.refreshToken,
        )
    }
}