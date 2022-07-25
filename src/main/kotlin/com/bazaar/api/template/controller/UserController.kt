package com.bazaar.api.template.controller

import com.bazaar.api.template.dto.AddUserRequest
import com.bazaar.api.template.dto.UpdateUserRequest
import com.bazaar.api.template.dto.response.UserResponse
import com.bazaar.api.template.service.UserServiceImpl
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/v1/user")
@PreAuthorize("hasAuthority('test.scope')")
class UserController(private val userServiceImpl: UserServiceImpl) {

    @GetMapping("/{id}")
    fun findById(@PathVariable id: String): ResponseEntity<UserResponse?> {
        val personResponse: UserResponse? = this.userServiceImpl.findById(id)
        return ResponseEntity.status(HttpStatus.OK).body(personResponse)
    }

    @GetMapping
    fun findAll(): ResponseEntity<List<UserResponse>> {
        return ResponseEntity.ok(this.userServiceImpl.findAll())
    }

    @PostMapping
    fun save(@RequestBody addPersonRequest: AddUserRequest): ResponseEntity<UserResponse> {
        val personResponse = this.userServiceImpl.save(addPersonRequest)
        return ResponseEntity
            .created(URI.create("/v1/user/".plus("/${personResponse.id}")))
            .body(personResponse)
    }

    @PutMapping
    fun update(@RequestBody updatePersonRequest: UpdateUserRequest): ResponseEntity<UserResponse> {
        return ResponseEntity.ok(this.userServiceImpl.update(updatePersonRequest))
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: String): ResponseEntity<Unit> {
        this.userServiceImpl.deleteById(id)
        return ResponseEntity.noContent().build();
    }
}