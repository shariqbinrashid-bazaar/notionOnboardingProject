package com.bazaar.api.template.service

import com.bazaar.api.template.dto.AddUserRequest
import com.bazaar.api.template.dto.UpdateUserRequest
import com.bazaar.api.template.dto.response.UserResponse
import com.bazaar.api.template.exception.FailedToFetchUserByIdException
import com.bazaar.api.template.exception.FailedToSaveUserException
import com.bazaar.api.template.exception.UserNotFoundException
import com.bazaar.api.template.model.UserModel
import com.bazaar.api.template.repository.UserRepository
import com.bazaar.api.template.transformer.AddUserRequestTransformer
import com.bazaar.api.template.transformer.toUserResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.stereotype.Service

@Service
class UserServiceImpl(private val userRepository: UserRepository,private val addUserRequestTransformer: AddUserRequestTransformer) :UserService{
    private val logger: Logger = LogManager.getLogger(UserServiceImpl::class.java)

    override fun findById(id: String): UserResponse? {

        try {
            val user=userRepository.findById(id).orElseThrow{
                throw UserNotFoundException(id)
            }
            return UserResponse(user.id!!,user.name,user.phone_number)
        }catch (ex:UserNotFoundException){
            throw ex
        } catch (ex: Exception) {
            val message = "Failed to fetch user by id= $id"
            logger.error(ex)
            throw FailedToFetchUserByIdException(message, ex)
        }
    }

    override fun findAll(): List<UserResponse> = this.userRepository.findAll().map(UserModel::toUserResponse)


    override fun save(addPersonRequest: AddUserRequest): UserResponse {
        try {
            val user=addUserRequestTransformer.transform(addPersonRequest)
            return userRepository.save(user).toUserResponse()
        } catch (ex: Exception) {
            logger.error(ex)
            val message = "Failed To Save user for user name: ${addPersonRequest.name}"
            throw FailedToSaveUserException(message, ex)
        }
    }

    override fun update(updateUserRequest: UpdateUserRequest): UserResponse {
        try{
            val user = this.userRepository.findById(updateUserRequest.id).orElseThrow{
                throw UserNotFoundException(updateUserRequest.id)
            }
            return userRepository.save(user.apply {
                this.name=updateUserRequest.name
                this.phone_number = updateUserRequest.phone_number
            }).toUserResponse()
        }catch (ex:UserNotFoundException){
            throw ex
        } catch (ex: Exception) {
            val message = "Failed to fetch user by id= ${updateUserRequest.id}"
            logger.error(ex)
            throw FailedToFetchUserByIdException(message, ex)
        }

    }

    override fun deleteById(id: String) {
        userRepository.deleteById(id as String)
    }


}

