package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.ResumoApi
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ResumoResponse
import dev.alexandrevieira.alurachallengebackend.service.ResumoService
import org.springframework.web.bind.annotation.RestController
import java.time.YearMonth

@RestController
class ResumoController(
    val service: ResumoService
) : ResumoApi {
    override fun gerarResumo(ano: Int, mes: Int): ResumoResponse {
        return service.resumoMensal(YearMonth.of(ano, mes))
    }
}