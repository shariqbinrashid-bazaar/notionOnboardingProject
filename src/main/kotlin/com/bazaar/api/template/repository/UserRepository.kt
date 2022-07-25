package com.bazaar.api.template.repository

import com.bazaar.api.template.model.UserModel
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserModel,String>