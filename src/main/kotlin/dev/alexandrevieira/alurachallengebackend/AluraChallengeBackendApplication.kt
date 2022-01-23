package dev.alexandrevieira.alurachallengebackend

import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import java.time.LocalDate
import java.time.YearMonth

@SpringBootApplication
class AluraChallengeBackendApplication: CommandLineRunner {
	override fun run(vararg args: String?) {
		val now = YearMonth.now()
		println(LocalDate.of(now.year, now.month, 1))
	}
}

fun main(args: Array<String>) {
	runApplication<AluraChallengeBackendApplication>(*args)
}
