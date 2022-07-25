package com.bazaar.api.template.exception

import com.bazaar.api.common.exception.BazaarRuntimeException

 class UserNotFoundException : BazaarRuntimeException{
     constructor(id: String) : super("Failed to fetch user by id= $id")
 }