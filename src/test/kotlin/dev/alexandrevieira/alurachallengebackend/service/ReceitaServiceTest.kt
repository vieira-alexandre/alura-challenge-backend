package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.GeneralBeans
import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.exception.UnprocessableEntityException
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.YearMonth


internal class ReceitaServiceTest : GeneralBeans() {

    @AfterEach
    fun tearDown() {
        receitaRepository.deleteAll()
    }

    @Test
    fun `deve cadastrar uma receita`() {
        val salario = super.getSalario(YearMonth.of(2022, 1))
        assertNull(salario.id)

        val cadastrada = receitaService.cadastrar(salario)

        assertNotNull(salario.id)
        assertEquals(salario.id, cadastrada.id)
        assertEquals(salario.descricao, cadastrada.descricao)
        assertEquals(salario.valor, cadastrada.valor)
        assertEquals(salario.mes, cadastrada.mes)

    }

    @Test
    fun `deve listar as receitas`() {
        val salario1 = super.getSalario(YearMonth.of(2022, 1))
        val freela2 = super.getFreela(YearMonth.of(2022, 2))
        receitaService.cadastrar(salario1)
        receitaService.cadastrar(freela2)

        val receitas: Page<Receita> = receitaService.listar(PageRequest.of(0, 10))

        assertEquals(2, receitas.totalElements)
    }

    @Test
    fun `deve buscar uma receita`() {
        val salario = super.getSalario(YearMonth.of(2022, 3))
        val descricaoEsperada = salario.descricao
        val valorEsperado = salario.valor
        val mesEsperado = salario.mes

        receitaService.cadastrar(salario)
        assertNotNull(salario.id)
        val idEsperado: Long = salario.id!!
        val receitaEncontrada = receitaService.detalhar(idEsperado)

        assertNotNull(receitaEncontrada)
        assertEquals(idEsperado, receitaEncontrada.id)
        assertEquals(descricaoEsperada, receitaEncontrada.descricao)
        assertEquals(valorEsperado.setScale(2), receitaEncontrada.valor.setScale(2))
        assertEquals(mesEsperado, receitaEncontrada.mes)
    }

    @Test
    fun `deve lancar excecao ao buscar uma receita inexistente`() {
        assertThrows(NotFoundException::class.java) {
            receitaService.detalhar(Long.MAX_VALUE)
        }
    }

    @Test
    fun `deve listar receitas por mes`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val salario1 = super.getSalario(mes1)
        val salario2 = super.getSalario(mes2)
        val aluguel2 = super.getReceitaAluguel(mes2)
        receitaService.cadastrar(salario1)
        receitaService.cadastrar(salario2)
        receitaService.cadastrar(aluguel2)

        val receitasMes1: Page<Receita> = receitaService.listarPorMes(PageRequest.of(0, 10), mes1)
        val receitasMes2: Page<Receita> = receitaService.listarPorMes(PageRequest.of(0, 10), mes2)

        assertEquals(1, receitasMes1.totalElements)
        assertEquals(2, receitasMes2.totalElements)
    }

    @Test
    fun `deve excluir uma receita`() {
        val freela = super.getFreela(YearMonth.of(2022, 2))

        assertNull(freela.id)

        receitaService.cadastrar(freela)
        assertNotNull(freela.id)

        assertDoesNotThrow {
            receitaService.detalhar(freela.id!!)
        }

        receitaService.excluir(freela.id!!)

        assertThrows(NotFoundException::class.java) {
            receitaService.detalhar(freela.id!!)
        }
    }

    @Test
    fun `deve lancar excecao ao tentar excluir uma receita que nao existe`() {
        assertThrows(NotFoundException::class.java) {
            receitaService.excluir(Long.MAX_VALUE)
        }
    }

    @Test
    fun `deve atualizar uma receita`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val freela1 = getFreela(mes1)
        val freela2 = getSalario(mes2)

        val receitaCadastrada = receitaService.cadastrar(freela1)
        assertNotNull(receitaCadastrada.id)
        val idFrellaCadastrado: Long = receitaCadastrada.id!!

        receitaService.atualizar(idFrellaCadastrado, freela2)
        val receitaAtualizada = receitaService.detalhar(receitaCadastrada.id!!)

        assertEquals(receitaCadastrada.id, receitaAtualizada.id)
        assertNotEquals(receitaCadastrada.descricao, receitaAtualizada.descricao)
        assertNotEquals(receitaCadastrada.mes, receitaAtualizada.mes)
        assertNotEquals(receitaCadastrada.valor.setScale(2), receitaAtualizada.valor.setScale(2))

        assertEquals(receitaCadastrada.descricao, freela1.descricao)
        assertEquals(receitaCadastrada.mes, freela1.mes)
        assertEquals(receitaCadastrada.valor.setScale(2), freela1.valor.setScale(2))

        assertEquals(receitaAtualizada.descricao, freela2.descricao)
        assertEquals(receitaAtualizada.mes, freela2.mes)
        assertEquals(receitaAtualizada.valor.setScale(2), freela2.valor.setScale(2))

        assertEquals(mes1, receitaCadastrada.mes)
        assertEquals(mes2, receitaAtualizada.mes)

    }

    @Test
    fun `deve lancar excecao ao tentar atualizar uma receita com o mes e descricao de outra`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val salarioMes1 = getSalario(mes1)
        val salarioMes2 = getSalario(mes2)
        val cadastradaMes1 = receitaService.cadastrar(salarioMes1)
        val cadastradaMes2 = receitaService.cadastrar(salarioMes2)

        assertNotNull(cadastradaMes1.id)
        assertNotNull(cadastradaMes2.id)
        val idSalarioMes1 = cadastradaMes1.id!!

        assertThrows(UnprocessableEntityException::class.java) {
            //tenta atualizar o salário do mês 1, com o mês e descrição do salário do mês 2 (que já existe)
            receitaService.atualizar(idSalarioMes1, salarioMes2)
        }
    }

    @Test
    fun `deve lancar excecao ao atualizar uma receita que nao existe`() {
        val receita = getSalario(YearMonth.of(2022, 1))

        assertThrows(NotFoundException::class.java) {
            receitaService.atualizar(Long.MAX_VALUE, receita)
        }
    }

    @Test
    fun `deve listar receitas com a descricao contendo`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val salario1 = super.getSalario(mes1)
        val salario2 = super.getSalario(mes2)
        val frella2 = super.getFreela(mes2)
        receitaService.cadastrar(salario1)
        receitaService.cadastrar(salario2)
        receitaService.cadastrar(frella2)

        val lari: Page<Receita> = receitaService.listarContendo(PageRequest.of(0, 10), "lári")
        val reel: Page<Receita> = receitaService.listarContendo(PageRequest.of(0, 10), "reel")

        assertEquals(2, lari.totalElements)
        assertEquals(1, reel.totalElements)
    }
}