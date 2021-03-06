package dev.alexandrevieira.alurachallengebackend.model.repositories

import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.YearMonth

@Repository
interface ReceitaRepository : JpaRepository<Receita, Long> {
    fun existsByDescricaoAndMes(descricao: String, mes: YearMonth): Boolean

    fun findByDescricaoContainingIgnoreCase(pageable: Pageable, descricao: String): Page<Receita>

    fun findByMes(pageable: Pageable, mes: YearMonth): Page<Receita>

    @Query("SELECT SUM(r.valor) FROM Receita r WHERE r.mes = :mes")
    fun getTotalMes(mes: YearMonth) : BigDecimal
}