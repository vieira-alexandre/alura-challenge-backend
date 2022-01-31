package dev.alexandrevieira.alurachallengebackend.model.entities

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.time.YearMonth

internal class ReceitaTest  {

    @Test
    fun `testa equals`() {
        val mes1 = YearMonth.of(2022, 1)
        val salario1 = Receita("Salário", 5000.00.toBigDecimal(), mes1)
        val salario2 = Receita("Salário", 5000.00.toBigDecimal(), mes1)

        val freela1 = Receita("Freella", 500.00.toBigDecimal(), mes1)
        val freela2 = Receita("Freela", 500.00.toBigDecimal(), mes1)

        Assertions.assertTrue(salario1 == salario2)
        Assertions.assertTrue(freela1 != freela2)
    }

    @Test
    fun `testa hash code`() {
        val mes1 = YearMonth.of(2022, 1)
        val salario1 = Receita("Salário", 5000.00.toBigDecimal(), mes1)
        val salario2 = Receita("Salário", 5000.00.toBigDecimal(), mes1)

        val freela1 = Receita("Freella", 500.00.toBigDecimal(), mes1)
        val freela2 = Receita("Freela", 500.00.toBigDecimal(), mes1)

        Assertions.assertEquals(salario1.hashCode(), salario2.hashCode())
        Assertions.assertNotEquals(freela1.hashCode(), freela2.hashCode())
    }
}