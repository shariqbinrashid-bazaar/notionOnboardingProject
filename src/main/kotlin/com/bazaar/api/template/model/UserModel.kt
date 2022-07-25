package com.bazaar.api.template.model

import com.devskiller.friendly_id.FriendlyId
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.LastModifiedBy
import java.time.LocalDateTime
import javax.persistence.*

@Entity
@Table(name = "user")
class UserModel (
    @Column(name = "name")
    var name: String = "",

    @Column(name = "phone_number")
    var phone_number: String ?= "",
):BaseEntity()