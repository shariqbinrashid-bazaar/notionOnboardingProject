package com.bazaar.api.template.model

import com.devskiller.friendly_id.FriendlyId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Size


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(id: String? = null) : Serializable {
    @Id
    @Column(name = "id")
    var id: @Size(max = 200) String?
        protected set

    @Column(name = "created_at", nullable = false, updatable = false)
    lateinit var createdAt: LocalDateTime

    @Column(name = "updated_at", nullable = false)
    lateinit var updatedAt: LocalDateTime

    @CreatedBy
    @Column(name = "created_by")
    var createdBy: String = ""

    @LastModifiedBy
    @Column(name = "updated_by")
    var updatedBy: String = ""

    @PrePersist
    fun prePersist() {
        createdAt = LocalDateTime.now()
        updatedAt = LocalDateTime.now()
    }

    @PreUpdate
    fun preUpdate() {
        updatedAt = LocalDateTime.now()
    }

    init {
        this.id = id ?: FriendlyId.createFriendlyId()
    }
}