package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.response.ResumoResponse
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RequestMapping("/resumo")
@Api(value = "resumo-api", tags = ["Resumo API"], description = "Responsável pelo resumo mensal")
interface ResumoApi {

    @GetMapping("/{ano}/{mes}")
    fun gerarResumo(@PathVariable @Min(2022) ano: Int, @PathVariable @Min(1) @Max(12) mes: Int): ResumoResponse
}