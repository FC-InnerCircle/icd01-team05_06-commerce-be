package com.commerce.common.persistence

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseTimeEntity {

    @Column(name = "createdAt")
    @CreatedDate
    var createdAt: LocalDateTime? = null

    @Column(name = "updatedAt")
    @LastModifiedDate
    var updatedAt: LocalDateTime? = null
}