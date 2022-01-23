package dev.alexandrevieira.alurachallengebackend.model.entities

import dev.alexandrevieira.alurachallengebackend.model.converters.YearMonthDateAttributeConverter
import java.math.BigDecimal
import java.time.YearMonth
import javax.persistence.*

@Entity
@Table(
    uniqueConstraints = [UniqueConstraint(
        name = "uk_despesa_descricao_mes",   // must define the constraint name properly
        columnNames = ["descricao", "mes"]
    )]
)
class Despesa(
    @Column(name = "descricao", nullable = false)
    var descricao: String,
    @Column(nullable = false, scale = 2, precision = 12)
    var valor: BigDecimal,
    @Column(nullable = false, columnDefinition = "date")
    @Convert(converter = YearMonthDateAttributeConverter::class)
    var mes: YearMonth,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
}