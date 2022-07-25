package com.bazaar.api.template.service
import com.bazaar.api.template.dto.AddUserRequest
import com.bazaar.api.template.dto.UpdateUserRequest
import org.springframework.data.domain.Page
import com.bazaar.api.template.dto.response.UserResponse
import org.springframework.data.domain.Pageable

interface UserService {
    fun findById(id:String): UserResponse?
    fun findAll(): Collection<UserResponse>
    fun save(addPersonRequest: AddUserRequest): UserResponse
    fun update(updatePersonRequest: UpdateUserRequest): UserResponse
    fun deleteById(id:String)
}