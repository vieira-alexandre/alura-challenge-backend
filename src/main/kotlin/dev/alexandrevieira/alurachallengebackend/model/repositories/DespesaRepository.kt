package dev.alexandrevieira.alurachallengebackend.model.repositories

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.YearMonth

@Repository
interface DespesaRepository : JpaRepository<Despesa, Long> {
    fun existsByDescricaoAndMes(descricao: String, mes: YearMonth): Boolean

    fun findByDescricaoContainingIgnoreCase(pageable: Pageable, descricao: String): Page<Despesa>
}