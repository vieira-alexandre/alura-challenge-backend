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
    val categoria: String,
) {
    companion object {
        fun of(despesa: Despesa): DespesaResponse {
            Assert.state(despesa.id != null, "Estado inválido: id não deve ser nulo")
            return DespesaResponse(
                id = despesa.id!!,
                descricao = despesa.descricao,
                valor = despesa.valor,
                mes = despesa.mes,
                categoria = despesa.categoria.name
            )
        }
    }
}