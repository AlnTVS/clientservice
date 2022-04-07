package com.alntvs.clientservice.controller

import com.alntvs.clientservice.model.DataDto
import com.alntvs.clientservice.model.ErrorDto
import org.springframework.core.MethodParameter
import org.springframework.http.MediaType
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.lang.Nullable
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice

@RestControllerAdvice
class CommonController : ResponseBodyAdvice<Any> {
    override fun supports(returnType: MethodParameter, converterType: Class<out HttpMessageConverter<*>>): Boolean {
        return true
    }

    override fun beforeBodyWrite(
        @Nullable body: Any?,
        returnType: MethodParameter,
        selectedContentType: MediaType,
        selectedConverterType: Class<out HttpMessageConverter<*>>,
        request: ServerHttpRequest,
        response: ServerHttpResponse
    ): Any? {
        body?.also {
            if (body.javaClass == ErrorDto::class.java) return body
        }
        val dataDto = DataDto()
        if (returnType.genericParameterType.typeName == "void") {
            dataDto.data = "success"
        } else {
            dataDto.data = body
        }
        return dataDto
    }
}