package com.bazaar.api.template.controller

import org.springframework.http.HttpStatus
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("v1/template")
class TemplateController {


    @PreAuthorize("hasAuthority('service.entity.scope.access')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    fun getTemplate() = "Template"

}
