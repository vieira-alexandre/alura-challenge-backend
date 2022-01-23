package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import io.swagger.annotations.Api
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/receitas")
@Api(value = "receita-api", tags = ["Receitas API"], description = "Responsável pelo gerenciamento de receitas")
interface ReceitaApi {

    @PostMapping
    fun cadastrar(@RequestBody request: NovaReceitaRequest): ResponseEntity<Unit>
}