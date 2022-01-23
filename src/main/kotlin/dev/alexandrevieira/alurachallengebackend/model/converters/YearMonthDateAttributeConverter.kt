package dev.alexandrevieira.alurachallengebackend.model.converters

import java.sql.Date
import java.time.Instant
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import javax.persistence.AttributeConverter

class YearMonthDateAttributeConverter : AttributeConverter<YearMonth, Date> {
    override fun convertToDatabaseColumn(attribute: YearMonth?): Date {
        val localDate = LocalDate.of(attribute!!.year, attribute.month, 1)
        return Date.valueOf(localDate)
    }

    override fun convertToEntityAttribute(dbData: Date?): YearMonth {
        val instant = Instant.ofEpochMilli(dbData!!.time)
        val localDate = LocalDate.ofInstant(instant, ZoneId.systemDefault())
        return YearMonth.of(localDate.year, localDate.month)
    }

}
