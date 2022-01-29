package dev.alexandrevieira.alurachallengebackend

import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import dev.alexandrevieira.alurachallengebackend.service.DespesaService
import dev.alexandrevieira.alurachallengebackend.service.ReceitaService
import dev.alexandrevieira.alurachallengebackend.service.ResumoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import java.time.YearMonth

@SpringBootTest
@ActiveProfiles("test")
class GeneralBeans {
    @Autowired
    lateinit var receitaRepository: ReceitaRepository

    @Autowired
    lateinit var despesaRepository: DespesaRepository

    @Autowired
    lateinit var receitaService: ReceitaService

    @Autowired
    lateinit var despesaService: DespesaService

    @Autowired
    lateinit var resumoService: ResumoService

    fun getSalario(mes: YearMonth) = Receita("Salário", 5000.00.toBigDecimal(), mes)

    fun getFreela(mes: YearMonth) = Receita("Freela", 1200.00.toBigDecimal(), mes)

    fun getReceitaAluguel(mes: YearMonth) = Receita("Aluguel", 900.00.toBigDecimal(), mes)

    fun getInvestimentos(mes: YearMonth) = Receita("Investimentos", 1500.00.toBigDecimal(), mes)

    fun getConveio(mes: YearMonth) =
        Despesa("Convênio", 250.00.toBigDecimal(), mes, Categoria.SAUDE)

    fun getCasa(mes: YearMonth) = Despesa("Casa", 1300.00.toBigDecimal(), mes, Categoria.MORADIA)

    fun getEnergia(mes: YearMonth) =
        Despesa("Energia", 350.00.toBigDecimal(), mes, Categoria.MORADIA)

    fun getCombustivel(mes: YearMonth) =
        Despesa("Combustível", 400.00.toBigDecimal(), mes, Categoria.TRANSPORTE)

    fun getSeguroAuto(mes: YearMonth) =
        Despesa("Seguro Auto", 1200.00.toBigDecimal(), mes, Categoria.TRANSPORTE)
}