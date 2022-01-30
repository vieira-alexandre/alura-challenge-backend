package dev.alexandrevieira.alurachallengebackend.model.entities

import dev.alexandrevieira.alurachallengebackend.model.converters.YearMonthDateAttributeConverter
import dev.alexandrevieira.alurachallengebackend.model.enums.Categoria
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
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    var categoria: Categoria,
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Despesa

        if (descricao != other.descricao) return false
        if (valor != other.valor) return false
        if (mes != other.mes) return false
        if (categoria != other.categoria) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = descricao.hashCode()
        result = 31 * result + valor.hashCode()
        result = 31 * result + mes.hashCode()
        result = 31 * result + categoria.hashCode()
        result = 31 * result + (id?.hashCode() ?: 0)
        return result
    }


}