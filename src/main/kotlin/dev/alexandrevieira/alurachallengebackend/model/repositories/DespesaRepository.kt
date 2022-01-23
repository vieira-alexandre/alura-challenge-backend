package dev.alexandrevieira.alurachallengebackend.model.repositories

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.YearMonth

@Repository
interface DespesaRepository : JpaRepository<Despesa, Long> {
    fun existsByDescricaoAndMes(descricao: String, mes: YearMonth): Boolean
}