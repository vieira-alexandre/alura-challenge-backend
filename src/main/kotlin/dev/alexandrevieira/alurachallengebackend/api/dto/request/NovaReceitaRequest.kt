package dev.alexandrevieira.alurachallengebackend.api.dto.request

import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import java.math.BigDecimal
import java.time.YearMonth

data class NovaReceitaRequest(
    val descricao: String,
    val valor: BigDecimal,
    val mes: YearMonth,
) {
    fun toModel(): Receita {
        return Receita(descricao = descricao, valor = valor, mes = mes)
    }
}