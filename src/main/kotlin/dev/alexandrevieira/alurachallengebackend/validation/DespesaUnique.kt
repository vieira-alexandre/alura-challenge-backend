package dev.alexandrevieira.alurachallengebackend.validation

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [DespesaUniqueValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
annotation class DespesaUnique(
    val message: String = "Despesa já cadastrada para o mês informado",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class DespesaUniqueValidator(
    private val repository: DespesaRepository,
) : ConstraintValidator<DespesaUnique, NovaDespesaRequest> {


    override fun isValid(value: NovaDespesaRequest?, context: ConstraintValidatorContext?): Boolean {
        return value == null || !repository.existsByDescricaoAndMes(value.descricao, value.mes)
    }
}