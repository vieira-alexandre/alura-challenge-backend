package dev.alexandrevieira.alurachallengebackend.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import dev.alexandrevieira.alurachallengebackend.BaseControllerTest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ResumoResponse
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import dev.alexandrevieira.alurachallengebackend.service.ResumoService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.YearMonth

internal class ResumoControllerTest : BaseControllerTest() {
    @MockBean
    private lateinit var service: ResumoService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `deve retornar o resumo mensal`() {
        val mes = 1
        val ano = 2022
        Mockito
            .`when`(service.resumoMensal(YearMonth.of(ano, mes)))
            .thenReturn(getResumo())

        val jsonString = ObjectMapper()
            .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
            .writeValueAsString(getResumo())

        mockMvc.perform(
            MockMvcRequestBuilders.get("/resumo/$ano/$mes")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content().json(jsonString)
        )

    }

    private fun getResumo(): ResumoResponse {
        val totalReceitas = 6500.00.toBigDecimal()
        val totalDespesas = 5029.90.toBigDecimal()

        val saldo = totalReceitas - totalDespesas
        val totalPorCategoria = mapOf(
            Categoria.MORADIA to 2100.00.toBigDecimal(),
            Categoria.TRANSPORTE to 600.00.toBigDecimal(),
            Categoria.ALIMENTACAO to 1400.00.toBigDecimal(),
            Categoria.SAUDE to 350.00.toBigDecimal(),
            Categoria.LAZER to 500.00.toBigDecimal(),
            Categoria.EDUCACAO to 79.90.toBigDecimal()
        )

        return ResumoResponse(totalReceitas, totalDespesas, saldo, totalPorCategoria)
    }
}