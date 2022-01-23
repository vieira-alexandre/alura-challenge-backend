package dev.alexandrevieira.alurachallengebackend.api.dto.response

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import org.springframework.util.Assert
import java.math.BigDecimal
import java.time.YearMonth

data class DespesaResponse(
    val id: Long,
    val descricao: String,
    val valor: BigDecimal,
    val mes: YearMonth,
) {
    companion object {
        fun of(despesa: Despesa): DespesaResponse {
            Assert.state(despesa.id != null, "Estado inválido: id não deve ser nulo")
            return DespesaResponse(despesa.id!!, despesa.descricao, despesa.valor, despesa.mes)
        }
    }
}