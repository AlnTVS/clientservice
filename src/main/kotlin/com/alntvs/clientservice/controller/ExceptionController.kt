package com.alntvs.clientservice.controller

import com.alntvs.clientservice.controller.ExceptionController
import com.alntvs.clientservice.exception.ClientServiceException
import com.alntvs.clientservice.model.ErrorDto
import org.apache.logging.log4j.LogManager
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.*

@RestControllerAdvice
class ExceptionController {
    @ExceptionHandler(Exception::class)
    fun unhandledException(e: Exception): Any {
        return serviceExceptions(e)
    }

    @ExceptionHandler(ClientServiceException::class)
    fun serviceExceptions(e: Exception): Any {
        val error = ErrorDto()
        val uuid = UUID.nameUUIDFromBytes(e.message!!.toByteArray())
        error.error = e.message + " ||UUID:" + uuid + "||"
        logger.error(e.message + e.cause)
        return error
    }

    companion object {
        private val logger = LogManager.getLogger(
            ExceptionController::class.java
        )
    }
}