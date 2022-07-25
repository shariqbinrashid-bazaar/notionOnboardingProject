package com.bazaar.api.template.exception

import com.bazaar.api.common.exception.BazaarRuntimeException


class FailedToSaveUserException(message: String, ex: Exception) : BazaarRuntimeException(message, ex) {

}