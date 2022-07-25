package com.bazaar.api.template.transformer

import com.bazaar.api.template.dto.AddUserRequest
import com.bazaar.api.template.model.UserModel
import org.springframework.stereotype.Component


@Component
class AddUserRequestTransformer: Transformer<AddUserRequest, UserModel> {
    override fun transform(source: AddUserRequest): UserModel {
        return UserModel(
            name = source.name,
            phone_number = source.phone_number
        )
    }
}