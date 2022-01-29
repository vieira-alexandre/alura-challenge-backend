package dev.alexandrevieira.alurachallengebackend.api.dto.response

import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import java.math.BigDecimal

data class ResumoResponse (
    val totalReceitas: BigDecimal,
    val totalDespesas: BigDecimal,
    val saldoNoMes: BigDecimal,
    val totalPorCategoria: Map<Categoria, BigDecimal>
)
