package dev.alexandrevieira.alurachallengebackend.model.repositories

import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.YearMonth

@Repository
interface ReceitaRepository : JpaRepository<Receita, Long> {
    fun existsByDescricaoAndMes(descricao: String, mes: YearMonth): Boolean
}