package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.api.dto.response.ResumoResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.YearMonth

@Service
class ResumoService(
    val receitaRepository: ReceitaRepository,
    val despesaRepository: DespesaRepository,
) {
    fun resumoMensal(mes: YearMonth): ResumoResponse {
        val totalPorCategoria: MutableMap<Categoria, BigDecimal> = mutableMapOf()
        val despesas: List<Despesa> = despesaRepository.findByMes(mes)
        val totalReceitas: BigDecimal = receitaRepository.getTotalMes(mes)
        val totalDespesas: BigDecimal = despesas.sumOf { it.valor }
        val saldoMes: BigDecimal = totalReceitas - totalDespesas

        Categoria.values().forEach { categoria ->
            val total: BigDecimal = despesas.filter { despesa -> despesa.categoria == categoria }.sumOf { it.valor }
            totalPorCategoria[categoria] = total
        }

        return ResumoResponse(
            totalDespesas = totalDespesas,
            totalReceitas = totalReceitas,
            saldoNoMes = saldoMes,
            totalPorCategoria = totalPorCategoria
        )
    }
}