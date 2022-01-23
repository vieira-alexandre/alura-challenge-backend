package dev.alexandrevieira.alurachallengebackend.exception.handler

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.http.HttpStatus

class ErrorsOutputDto(httpStatus: HttpStatus) {
    @field:JsonProperty
    val status: Int = httpStatus.value()

    @field:JsonProperty
    val globalErrorMessages: MutableList<String> = ArrayList()

    @field:JsonProperty
    val fieldErrors: MutableList<FieldErrorOutputDto> = ArrayList()

    constructor(httpStatus: HttpStatus, message: String) : this(httpStatus) {
        globalErrorMessages.add(message)
    }

    fun addError(message: String) {
        globalErrorMessages.add(message)
    }

    fun addFieldError(field: String?, message: String?) {
        val fieldError = FieldErrorOutputDto(field!!, message!!)
        fieldErrors.add(fieldError)
    }
}