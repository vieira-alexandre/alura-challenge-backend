package dev.alexandrevieira.alurachallengebackend.exception.handler

import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.exception.UnprocessableEntityException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.util.function.Consumer
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class GlobalExceptionHandler {
    @Autowired
    private val messageSource: MessageSource? = null

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(ex: MethodArgumentNotValidException): ErrorsOutputDto {
        return buildValidationErrors(ex.bindingResult.globalErrors, ex.bindingResult.fieldErrors)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException::class)
    fun handleBindException(ex: BindException): ErrorsOutputDto {
        return buildValidationErrors(ex.bindingResult.globalErrors, ex.bindingResult.fieldErrors)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(UnprocessableEntityException::class)
    fun handleUnprocessableEntityException(ex: UnprocessableEntityException): ErrorsOutputDto {
        return ErrorsOutputDto(HttpStatus.UNPROCESSABLE_ENTITY, ex.localizedMessage)
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolationException(ex: ConstraintViolationException): ErrorsOutputDto {
        return ErrorsOutputDto(HttpStatus.UNPROCESSABLE_ENTITY).let { output ->
            ex.constraintViolations.forEach { violation -> output.addError(violation.message) }
            output
        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException::class)
    fun handleNotFoundException(ex: NotFoundException): ErrorsOutputDto {
        return ErrorsOutputDto(HttpStatus.NOT_FOUND, ex.localizedMessage)
    }

    private fun buildValidationErrors(
        globalErrors: List<ObjectError>,
        fieldErrors: List<FieldError>,
    ): ErrorsOutputDto {
        val validationErrors = ErrorsOutputDto(HttpStatus.BAD_REQUEST)
        globalErrors.forEach(Consumer { error: ObjectError ->
            validationErrors.addError(getErrorMessage(error))
        })
        fieldErrors.forEach(Consumer { error: FieldError ->
            val errorMessage = getErrorMessage(error)
            validationErrors.addFieldError(error.field, errorMessage)
        })
        return validationErrors
    }

    private fun getErrorMessage(error: ObjectError): String {
        return messageSource!!.getMessage(error, LocaleContextHolder.getLocale())
    }
}