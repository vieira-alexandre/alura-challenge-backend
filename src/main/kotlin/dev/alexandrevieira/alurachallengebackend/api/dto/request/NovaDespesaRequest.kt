package dev.alexandrevieira.alurachallengebackend.api.dto.request

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import dev.alexandrevieira.alurachallengebackend.validation.DespesaUnique
import java.math.BigDecimal
import java.time.YearMonth
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

@DespesaUnique
data class NovaDespesaRequest(
    @field:NotBlank
    val descricao: String,

    @field:NotNull
    @field:Positive
    val valor: BigDecimal,

    @field:NotNull
    val mes: YearMonth,

    val categoria: Categoria?,

    ) {
    fun toModel(): Despesa {
        return Despesa(descricao = descricao, valor = valor, mes = mes, categoria = categoria ?: Categoria.OUTRAS)
    }
}