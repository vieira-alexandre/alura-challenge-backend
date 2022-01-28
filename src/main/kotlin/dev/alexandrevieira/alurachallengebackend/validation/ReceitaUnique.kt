package dev.alexandrevieira.alurachallengebackend.validation

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import javax.validation.Constraint
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext
import javax.validation.Payload
import kotlin.reflect.KClass

@Constraint(validatedBy = [ReceitaUniqueValidator::class])
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.VALUE_PARAMETER)
annotation class ReceitaUnique(
    val message: String = "Receita já cadastrada para o mês informado",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = [],
)

class ReceitaUniqueValidator(
    private val repository: ReceitaRepository,
) : ConstraintValidator<ReceitaUnique, NovaReceitaRequest> {


    override fun isValid(value: NovaReceitaRequest?, context: ConstraintValidatorContext?): Boolean {
        return value == null || !repository.existsByDescricaoAndMes(value.descricao, value.mes)
    }
}