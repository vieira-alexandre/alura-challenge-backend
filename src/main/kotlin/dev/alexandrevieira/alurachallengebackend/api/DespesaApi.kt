package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/despesas")
@Api(value = "despesa-api", tags = ["Despesas API"], description = "Responsável pelo gerenciamento de despesas")
interface DespesaApi {
    @PostMapping
    fun cadastrar(@RequestBody request: NovaDespesaRequest): ResponseEntity<Unit>
}