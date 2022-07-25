package com.bazaar.api.template.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserResponse  (val id:String, val name:String?,val phone_number:String?)