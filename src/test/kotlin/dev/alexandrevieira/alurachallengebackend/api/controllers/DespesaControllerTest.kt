package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.BaseControllerTest
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.DespesaResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import dev.alexandrevieira.alurachallengebackend.util.toJson
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.YearMonth
import java.util.*

internal class DespesaControllerTest : BaseControllerTest() {

    @MockBean
    private lateinit var repository: DespesaRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `deve cadastrar uma despesa`() {
        val mes = YearMonth.of(2022, 1)
        val despesaInput: Despesa = getEnergia(mes)
        val despesaOutput: Despesa = getEnergia(mes)
        despesaOutput.id = 42
        Mockito.`when`(repository.save(despesaInput)).thenReturn(despesaOutput)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/despesas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(getNovaDespesaRequest(mes).toJson())
        ).andExpect(
            MockMvcResultMatchers.status().isCreated
        ).andExpect(
            MockMvcResultMatchers.header().exists("location")
        ).andDo {
            val location = it.response.getHeaderValue("location")
            Assertions.assertTrue(location is String)
            location as String
            Assertions.assertTrue(location.contains("/despesas/42"))
        }
    }

    @Test
    fun `deve listar as despesas`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val energia1 = getEnergia(YearMonth.of(2022, 1))
        energia1.id = 42
        val energia2 = getEnergia(YearMonth.of(2022, 2))
        energia2.id = 99
        val despesas: MutableList<Despesa> = mutableListOf(energia1, energia2)
        val pagina: Page<Despesa> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<DespesaResponse> = pagina.map { DespesaResponse.of(it) }

        Mockito
            .`when`(repository.findAll(pageable))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/despesas")
                .param("size", "10")
                .param("page", "0")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content()
                .json(resultado.toJson())
        )
    }

    @Test
    fun `deve listar as despesas contendo na descricao`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val energia1 = getEnergia(YearMonth.of(2022, 1))
        energia1.id = 42
        val energia2 = getEnergia(YearMonth.of(2022, 2))
        energia2.id = 99
        val despesas: MutableList<Despesa> = mutableListOf(energia1, energia2)
        val pagina: Page<Despesa> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<DespesaResponse> = pagina.map { DespesaResponse.of(it) }

        Mockito
            .`when`(repository.findByDescricaoContainingIgnoreCase(pageable, "energ"))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/despesas")
                .param("size", "10")
                .param("page", "0")
                .param("descricao", "energ")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content()
                .json(resultado.toJson())
        )
    }

    @Test
    fun `deve buscar uma despesa`() {
        val energia = getEnergia(YearMonth.of(2022, 1))
        energia.id = 42
        Mockito.`when`(repository.findById(42)).thenReturn(Optional.of(energia))

        val result = DespesaResponse.of(energia).toJson()

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/despesas/42")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content().json(result)
        )
    }

    @Test
    fun `deve listar por mes`() {
        val mes = YearMonth.of(2022, 1)
        val pageable: Pageable = PageRequest.of(0, 10)
        val energia = getEnergia(mes)
        energia.id = 42
        val convenio = Despesa("Convênio", 250.00.toBigDecimal(), mes, Categoria.SAUDE)
        convenio.id = 99
        val despesas: MutableList<Despesa> = mutableListOf(energia, convenio)
        val pagina: Page<Despesa> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<DespesaResponse> = pagina.map { DespesaResponse.of(it) }

        Mockito
            .`when`(repository.findByMes(pageable, mes))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/despesas/${mes.year}/${mes.monthValue}")
                .param("size", "10")
                .param("page", "0")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content()
                .json(resultado.toJson())
        )
    }

    @Test
    fun `deve lancar excecao ao tentar listar um ano invalido`() {
        val ano = 1000000000
        val mess = 1

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/despesas/$ano/$mess")
                .param("size", "10")
                .param("page", "0")
        ).andExpect(
            MockMvcResultMatchers.status().isUnprocessableEntity
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.status", Matchers.`is`(422))
        )
    }

    @Test
    fun `deve excluir uma despesa`() {
        Mockito.`when`(repository.existsById(42)).thenReturn(true)
        Mockito.doNothing().`when`(repository).deleteById(42)

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/despesas/42")
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent
        )
    }

    @Test
    fun `deve atualizar uma despesa`() {
        val energia = getEnergia(YearMonth.of(2022, 1))
        energia.id = 42
        Mockito.`when`(repository.findById(42)).thenReturn(Optional.of(energia))


        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/despesas/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(energia.toJson())
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent
        )
    }

    private fun getNovaDespesaRequest(mes: YearMonth): NovaDespesaRequest {
        val energia = getEnergia(mes)
        return NovaDespesaRequest(
            descricao = energia.descricao,
            valor = energia.valor,
            mes = energia.mes,
            categoria = energia.categoria
        )
    }

    private fun getEnergia(mes: YearMonth): Despesa {
        return Despesa(descricao = "Energia", valor = 350.00.toBigDecimal(), mes = mes, categoria = Categoria.MORADIA)
    }
}