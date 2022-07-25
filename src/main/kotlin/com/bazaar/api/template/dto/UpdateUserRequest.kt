package com.bazaar.api.template.dto

data class UpdateUserRequest (val id: String, val name:String, val phone_number:String? = null)