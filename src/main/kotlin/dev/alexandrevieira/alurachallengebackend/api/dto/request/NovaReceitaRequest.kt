package dev.alexandrevieira.alurachallengebackend.api.dto.request

import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import java.math.BigDecimal
import java.time.YearMonth
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Positive

data class NovaReceitaRequest(
    @field:NotBlank
    val descricao: String,

    @field:NotNull
    @field:Positive
    val valor: BigDecimal,

    @field:NotNull
    val mes: YearMonth,
) {
    fun toModel(): Receita {
        return Receita(descricao = descricao, valor = valor, mes = mes)
    }
}