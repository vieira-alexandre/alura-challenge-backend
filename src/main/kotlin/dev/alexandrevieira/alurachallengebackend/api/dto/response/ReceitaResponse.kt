package dev.alexandrevieira.alurachallengebackend.api.dto.response

import com.fasterxml.jackson.annotation.JsonFormat
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import org.springframework.util.Assert
import java.math.BigDecimal
import java.time.YearMonth

data class ReceitaResponse(
    val id: Long,
    val descricao: String,
    val valor: BigDecimal,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-yyyy")
    val mes: YearMonth,
) {
    companion object {
        fun of(receita: Receita): ReceitaResponse {
            Assert.state(receita.id != null, "Estado inválido: id não deve ser nulo")
            return ReceitaResponse(receita.id!!, receita.descricao, receita.valor, receita.mes)
        }
    }
}



