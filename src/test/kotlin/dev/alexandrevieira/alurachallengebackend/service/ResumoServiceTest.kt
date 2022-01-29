package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.GeneralBeans
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ResumoResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.YearMonth

internal class ResumoServiceTest : GeneralBeans() {

    @AfterEach
    fun tearDown() {
        despesaRepository.deleteAll()
        receitaRepository.deleteAll()
    }

    @Test
    fun resumoMensal() {
        // mês 1
        val mes1 = YearMonth.of(2022, 1)
        val receitas1: List<Receita> = setupReceitas(getSalario(mes1), getFreela(mes1), getInvestimentos(mes1))
        val despesas1: List<Despesa> = setupDespesas(getConveio(mes1), getCombustivel(mes1))
        val totalReceitas1: BigDecimal = receitas1.sumOf { it.valor }.setScale(2)
        val totalDespesas1: BigDecimal = despesas1.sumOf { it.valor }.setScale(2)
        val saldoMes1 = (receitas1.sumOf { it.valor } - despesas1.sumOf { it.valor }).setScale(2)
        val despesasMoradia1 = despesas1.filter { it.categoria == Categoria.MORADIA }.sumOf { it.valor }.setScale(2)
        val despesasSaude1 = despesas1.filter { it.categoria == Categoria.SAUDE }.sumOf { it.valor }.setScale(2)
        val despesasTransp1 = despesas1.filter { it.categoria == Categoria.TRANSPORTE }.sumOf { it.valor }.setScale(2)
        val resumoMes1: ResumoResponse = resumoService.resumoMensal(mes1)

        // mês 2
        val mes2 = YearMonth.of(2022, 2)
        val receitas2: List<Receita> = setupReceitas(getSalario(mes2), getInvestimentos(mes2))
        val despesas2: List<Despesa> =
            setupDespesas(getCasa(mes2), getEnergia(mes2), getConveio(mes2), getCombustivel(mes2), getSeguroAuto(mes2))
        val totalReceitas2: BigDecimal = receitas2.sumOf { it.valor }.setScale(2)
        val totalDespesas2: BigDecimal = despesas2.sumOf { it.valor }.setScale(2)
        val saldoMes2 = (receitas2.sumOf { it.valor } - despesas2.sumOf { it.valor }).setScale(2)
        val despesasMoradia2 = despesas2.filter { it.categoria == Categoria.MORADIA }.sumOf { it.valor }.setScale(2)
        val despesasSaude2 = despesas2.filter { it.categoria == Categoria.SAUDE }.sumOf { it.valor }.setScale(2)
        val despesasTransp2 = despesas2.filter { it.categoria == Categoria.TRANSPORTE }.sumOf { it.valor }.setScale(2)
        val resumoMes2: ResumoResponse = resumoService.resumoMensal(mes2)

        //asserções mês 1
        assertEquals(totalReceitas1, resumoMes1.totalReceitas.setScale(2))
        assertEquals(totalDespesas1, resumoMes1.totalDespesas.setScale(2))
        assertEquals(saldoMes1, resumoMes1.saldoNoMes.setScale(2))
        assertEquals(despesasMoradia1, resumoMes1.totalPorCategoria[Categoria.MORADIA]!!.setScale(2))
        assertEquals(despesasSaude1, resumoMes1.totalPorCategoria[Categoria.SAUDE]!!.setScale(2))
        assertEquals(despesasTransp1, resumoMes1.totalPorCategoria[Categoria.TRANSPORTE]!!.setScale(2))

        //asserções mês 2
        assertEquals(totalReceitas2, resumoMes2.totalReceitas.setScale(2))
        assertEquals(totalDespesas2, resumoMes2.totalDespesas.setScale(2))
        assertEquals(saldoMes2, resumoMes2.saldoNoMes.setScale(2))
        assertEquals(despesasMoradia2, resumoMes2.totalPorCategoria[Categoria.MORADIA]!!.setScale(2))
        assertEquals(despesasSaude2, resumoMes2.totalPorCategoria[Categoria.SAUDE]!!.setScale(2))
        assertEquals(despesasTransp2, resumoMes2.totalPorCategoria[Categoria.TRANSPORTE]!!.setScale(2))
    }

    private fun setupReceitas(vararg receitas: Receita): MutableList<Receita> {
        val cadastradas = mutableListOf<Receita>()
        receitas.forEach {
            cadastradas.add(receitaService.cadastrar(it))
        }
        return cadastradas
    }

    private fun setupDespesas(vararg despesas: Despesa): MutableList<Despesa> {
        val cadastradas = mutableListOf<Despesa>()
        despesas.forEach {
            cadastradas.add(despesaService.cadastrar(it))
        }
        return cadastradas
    }
}