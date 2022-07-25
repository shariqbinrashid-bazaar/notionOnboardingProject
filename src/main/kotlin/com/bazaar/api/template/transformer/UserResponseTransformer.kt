package com.bazaar.api.template.transformer

import com.bazaar.api.template.dto.response.UserResponse
import com.bazaar.api.template.model.UserModel

fun UserModel?.toUserResponse(): UserResponse {
    return UserResponse(
        id = this?.id ?: "",
        name = this?.name,
        phone_number=this?.phone_number
    )
}