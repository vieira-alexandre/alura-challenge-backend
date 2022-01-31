package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.BaseControllerTest
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ReceitaResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import dev.alexandrevieira.alurachallengebackend.util.toJson
import org.hamcrest.Matchers
import org.junit.jupiter.api.Assertions.assertTrue
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

internal class ReceitaControllerTest : BaseControllerTest() {
    @MockBean
    private lateinit var repository: ReceitaRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `deve cadastrar uma receita`() {
        val mes = YearMonth.of(2022, 1)
        val receitaInput: Receita = getSalario(mes)
        val receitaOutput: Receita = getSalario(mes)
        receitaOutput.id = 42
        Mockito.`when`(repository.save(receitaInput)).thenReturn(receitaOutput)

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/receitas")
                .content(getNovaReceitaRequest(mes).toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isCreated
        ).andExpect(
            MockMvcResultMatchers.header().exists("location")
        ).andDo {
            val location = it.response.getHeaderValue("location")
            assertTrue(location is String)
            location as String
            assertTrue(location.contains("/receitas/42"))
        }
    }

    @Test
    fun `deve retornar 400 ao enviar uma requisicao invalida`() {
        val mes = YearMonth.of(2022, 1)
        val receitaInput: Receita = getSalario(mes)
        Mockito.`when`(repository.existsByDescricaoAndMes(receitaInput.descricao, receitaInput.mes)).thenReturn(false)

        val request = object {
            val descricao = ""
            val valor = -5000.00
            val mes = "2022-01"
        }

        mockMvc.perform(
            MockMvcRequestBuilders
                .post("/receitas")
                .content(request.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isBadRequest
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.status", Matchers.`is`(400))
        )
    }


    @Test
    fun `deve listar as receitas`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val salario1 = getSalario(YearMonth.of(2022, 1))
        salario1.id = 42
        val salario2 = getSalario(YearMonth.of(2022, 2))
        salario2.id = 99
        val despesas: MutableList<Receita> = mutableListOf(salario1, salario2)
        val pagina: Page<Receita> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<ReceitaResponse> = pagina.map { ReceitaResponse.of(it) }

        Mockito
            .`when`(repository.findAll(pageable))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/receitas")
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
    fun `deve listar as receitas contendo na descricao`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val salario1 = getSalario(YearMonth.of(2022, 1))
        salario1.id = 42
        val salario2 = getSalario(YearMonth.of(2022, 2))
        salario2.id = 99
        val despesas: MutableList<Receita> = mutableListOf(salario1, salario2)
        val pagina: Page<Receita> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<ReceitaResponse> = pagina.map { ReceitaResponse.of(it) }

        Mockito
            .`when`(repository.findByDescricaoContainingIgnoreCase(pageable, "sal"))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/receitas")
                .param("size", "10")
                .param("page", "0")
                .param("descricao", "sal")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content()
                .json(resultado.toJson())
        )
    }

    @Test
    fun `deve buscar uma receita`() {
        val salario = getSalario(YearMonth.of(2022, 1))
        salario.id = 42
        Mockito.`when`(repository.findById(42)).thenReturn(Optional.of(salario))

        val result = ReceitaResponse.of(salario).toJson()

        mockMvc.perform(
            MockMvcRequestBuilders.get("/receitas/42")
        ).andExpect(
            MockMvcResultMatchers.status().isOk
        ).andExpect(
            MockMvcResultMatchers.content().json(result)
        )
    }

    @Test
    fun `deve retornar 404 ao buscar uma receita que nao existe`() {
        Mockito.`when`(repository.findById(42)).thenReturn(Optional.empty())

        mockMvc.perform(
            MockMvcRequestBuilders.get("/receitas/42")
        ).andExpect(
            MockMvcResultMatchers.status().isNotFound
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.status", Matchers.`is`(404))
        )
    }

    @Test
    fun `deve listar por mes`() {
        val mes = YearMonth.of(2022, 1)
        val pageable: Pageable = PageRequest.of(0, 10)
        val salario = getSalario(mes)
        salario.id = 42
        val freela = Receita("Freela", 1500.00.toBigDecimal(), mes)
        freela.id = 99
        val despesas: MutableList<Receita> = mutableListOf(salario, freela)
        val pagina: Page<Receita> = PageImpl(despesas, pageable, despesas.size.toLong())
        val resultado: Page<ReceitaResponse> = pagina.map { ReceitaResponse.of(it) }

        Mockito
            .`when`(repository.findByMes(pageable, mes))
            .thenReturn(pagina)

        mockMvc.perform(
            MockMvcRequestBuilders
                .get("/receitas/${mes.year}/${mes.monthValue}")
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
                .get("/receitas/$ano/$mess")
                .param("size", "10")
                .param("page", "0")
        ).andExpect(
            MockMvcResultMatchers.status().isUnprocessableEntity
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.status", Matchers.`is`(422))
        )
    }

    @Test
    fun `deve excluir uma receita`() {
        Mockito.`when`(repository.existsById(42)).thenReturn(true)
        Mockito.doNothing().`when`(repository).deleteById(42)

        mockMvc.perform(
            MockMvcRequestBuilders
                .delete("/receitas/42")
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent
        )
    }

    @Test
    fun `deve atualizar uma receita`() {
        val salario = getSalario(YearMonth.of(2022, 1))
        salario.id = 42
        Mockito.`when`(repository.findById(42)).thenReturn(Optional.of(salario))


        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/receitas/42")
                .contentType(MediaType.APPLICATION_JSON)
                .content(salario.toJson())
        ).andExpect(
            MockMvcResultMatchers.status().isNoContent
        )
    }


    @Test
    fun `deve retornar 422 ao atualizar uma receita com dados de outra existente`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val salario1: Receita = getSalario(mes1)
        salario1.id = 42
        val salario2: Receita = getSalario(mes2)
        salario2.id = 99

        val request = NovaReceitaRequest(salario1.descricao, salario1.valor, salario1.mes)

        Mockito.`when`(repository.existsByDescricaoAndMes(salario1.descricao, salario1.mes)).thenReturn(true)
        Mockito.`when`(repository.findById(salario1.id!!)).thenReturn(Optional.of(salario1))
        Mockito.`when`(repository.findById(salario2.id!!)).thenReturn(Optional.of(salario2))

        mockMvc.perform(
            MockMvcRequestBuilders
                .put("/receitas/${salario2.id}")
                .content(request.toJson())
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
            MockMvcResultMatchers.status().isUnprocessableEntity
        ).andExpect(
            MockMvcResultMatchers.jsonPath("$.status", Matchers.`is`(422))
        )
    }

    private fun getNovaReceitaRequest(mes: YearMonth): NovaReceitaRequest {
        val salario = getSalario(mes)
        return NovaReceitaRequest(
            descricao = salario.descricao,
            valor = salario.valor,
            mes = salario.mes
        )
    }

    private fun getSalario(mes: YearMonth): Receita {
        return Receita(descricao = "Salário", valor = 5000.00.toBigDecimal(), mes = mes)
    }
}