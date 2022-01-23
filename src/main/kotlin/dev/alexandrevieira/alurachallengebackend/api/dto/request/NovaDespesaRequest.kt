package dev.alexandrevieira.alurachallengebackend.api.dto.request

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import java.math.BigDecimal
import java.time.YearMonth

data class NovaDespesaRequest(
    val descricao: String,
    val valor: BigDecimal,
    val mes: YearMonth,
) {
    fun toModel(): Despesa {
        return Despesa(descricao = descricao, valor = valor, mes = mes)
    }
}