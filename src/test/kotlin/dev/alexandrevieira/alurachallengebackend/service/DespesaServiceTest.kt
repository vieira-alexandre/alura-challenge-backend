package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.GeneralBeans
import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.exception.UnprocessableEntityException
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import java.time.YearMonth


internal class DespesaServiceTest : GeneralBeans() {

    @AfterEach
    fun tearDown() {
        despesaRepository.deleteAll()
    }

    @Test
    fun `deve cadastrar uma despesa`() {
        val energia = super.getEnergia(YearMonth.of(2022, 2))

        assertNull(energia.id)

        val cadastrada = despesaService.cadastrar(energia)

        assertNotNull(energia.id)
        assertEquals(energia.id, cadastrada.id)
        assertEquals(energia.descricao, cadastrada.descricao)
        assertEquals(energia.valor, cadastrada.valor)
        assertEquals(energia.mes, cadastrada.mes)

    }

    @Test
    fun `deve listar as despesas`() {
        val combustivel = super.getCombustivel(YearMonth.of(2022, 1))
        val energia = super.getEnergia(YearMonth.of(2022, 2))
        despesaService.cadastrar(combustivel)
        despesaService.cadastrar(energia)

        val despesas: Page<Despesa> = despesaService.listar(PageRequest.of(0, 10))

        assertEquals(2, despesas.totalElements)
    }

    @Test
    fun `deve buscar uma despesa`() {
        val combustivel = super.getCombustivel(YearMonth.of(2022, 2))
        val descricaoEsperada = combustivel.descricao
        val valorEsperado = combustivel.valor
        val mesEsperado = combustivel.mes
        val categoriaEsperada = combustivel.categoria

        despesaService.cadastrar(combustivel)
        assertNotNull(combustivel.id)
        val idEsperado: Long = combustivel.id!!
        val despesaEncontrada = despesaService.detalhar(idEsperado)

        assertNotNull(despesaEncontrada)
        assertEquals(idEsperado, despesaEncontrada.id)
        assertEquals(descricaoEsperada, despesaEncontrada.descricao)
        assertEquals(valorEsperado.setScale(2), despesaEncontrada.valor.setScale(2))
        assertEquals(mesEsperado, despesaEncontrada.mes)
        assertEquals(categoriaEsperada, despesaEncontrada.categoria)
    }

    @Test
    fun `deve lancar excecao ao buscar uma despesa inexistente`() {
        assertThrows(NotFoundException::class.java) {
            despesaService.detalhar(Long.MAX_VALUE)
        }
    }

    @Test
    fun `deve listar despesas por mes`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val combustivel = super.getCombustivel(mes1)
        val energia = super.getEnergia(mes2)
        val convenio = super.getConvenio(mes2)
        despesaService.cadastrar(combustivel)
        despesaService.cadastrar(energia)
        despesaService.cadastrar(convenio)

        val despesas1: Page<Despesa> = despesaService.listarPorMes(PageRequest.of(0, 10), mes1)
        val despesas2: Page<Despesa> = despesaService.listarPorMes(PageRequest.of(0, 10), mes2)

        assertEquals(1, despesas1.totalElements)
        assertEquals(2, despesas2.totalElements)
    }

    @Test
    fun `deve excluir uma despesa`() {
        val energia = super.getEnergia(YearMonth.of(2022, 2))

        assertNull(energia.id)

        despesaService.cadastrar(energia)
        assertNotNull(energia.id)

        assertDoesNotThrow {
            despesaService.detalhar(energia.id!!)
        }

        despesaService.excluir(energia.id!!)

        assertThrows(NotFoundException::class.java) {
            despesaService.detalhar(energia.id!!)
        }
    }

    @Test
    fun `deve lancar excecao ao tentar excluir uma despesa que nao existe`() {
        assertThrows(NotFoundException::class.java) {
            despesaService.excluir(Long.MAX_VALUE)
        }
    }

    @Test
    fun `deve atualizar uma despesa`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val despesa1 = getEnergia(mes1)
        val despesa2 = getEnergia(mes2)
        val despesaCadastrada = despesaService.cadastrar(despesa1)

        assertNotNull(despesaCadastrada.id)

        despesaService.atualizar(despesaCadastrada.id!!, despesa2)
        val despesaAtualizada = despesaService.detalhar(despesaCadastrada.id!!)

        assertEquals(despesaCadastrada.id, despesaAtualizada.id)
        assertEquals(despesaCadastrada.descricao, despesaAtualizada.descricao)
        assertEquals(despesaCadastrada.valor.setScale(2), despesaAtualizada.valor.setScale(2))
        assertEquals(mes1, despesaCadastrada.mes)
        assertEquals(mes2, despesaAtualizada.mes)

    }

    @Test
    fun `deve lancar excecao ao tentar atualizar uma despesa com o mes e descricao de outra`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val energiaMes1 = getEnergia(mes1)
        val energiaMes2 = getEnergia(mes2)
        val cadastradaMes1 = despesaService.cadastrar(energiaMes1)
        val cadastradaMes2 = despesaService.cadastrar(energiaMes2)

        assertNotNull(cadastradaMes1.id)
        assertNotNull(cadastradaMes2.id)
        val idEnergiaMes1 = cadastradaMes1.id!!

        assertThrows(UnprocessableEntityException::class.java) {
            //tenta atualizar a energia do mês 1, com o mês e descrição da energia do mês 2 (que já existe)
            despesaService.atualizar(idEnergiaMes1, energiaMes2)
        }
    }

    @Test
    fun `deve lancar excecao ao atualizar uma despesa que nao existe`() {
        val despesa1 = getEnergia(YearMonth.of(2022, 1))

        assertThrows(NotFoundException::class.java) {
            despesaService.atualizar(Long.MAX_VALUE, despesa1)
        }
    }

    @Test
    fun `deve listar despesas com a descricao contendo`() {
        val mes1 = YearMonth.of(2022, 1)
        val mes2 = YearMonth.of(2022, 2)
        val combustivel1 = super.getCombustivel(mes1)
        val combustivel2 = super.getCombustivel(mes2)
        val energia = super.getEnergia(mes2)
        despesaService.cadastrar(combustivel1)
        despesaService.cadastrar(combustivel2)
        despesaService.cadastrar(energia)

        val ombustiv: Page<Despesa> = despesaService.listarContendo(PageRequest.of(0, 10), "ombustív")
        val nergi: Page<Despesa> = despesaService.listarContendo(PageRequest.of(0, 10), "nergi")

        assertEquals(2, ombustiv.totalElements)
        assertEquals(1, nergi.totalElements)
    }
}