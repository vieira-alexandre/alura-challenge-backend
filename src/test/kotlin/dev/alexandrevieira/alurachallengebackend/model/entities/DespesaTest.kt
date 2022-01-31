package dev.alexandrevieira.alurachallengebackend.model.entities

import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.YearMonth

internal class DespesaTest {

    @Test
    fun `testa equals`() {
        val mes1 = YearMonth.of(2022, 1)
        val casa1 = Despesa("Casa", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        val casa2 = Despesa("Casa", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)

        val convenio1 = Despesa("Convênio", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        val convenio2 = Despesa("Convênio", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        convenio2.id = 1

        assertTrue(casa1 == casa2)
        assertTrue(convenio1 != convenio2)
    }

    @Test
    fun `testa hash code`() {
        val mes1 = YearMonth.of(2022, 1)

        val casa1 = Despesa("Casa", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        val casa2 = Despesa("Casa", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)

        val convenio1 = Despesa("Convênio", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        val convenio2 = Despesa("Convênio", 500.00.toBigDecimal(), mes1, Categoria.MORADIA)
        convenio2.id = 1

        assertEquals(casa1.hashCode() , casa2.hashCode())
        assertNotEquals(convenio1.hashCode() , convenio2.hashCode())
    }
}