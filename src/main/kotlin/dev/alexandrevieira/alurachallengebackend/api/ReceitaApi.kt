package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ReceitaResponse
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RequestMapping("/receitas")
@Api(value = "receita-api", tags = ["Receitas API"], description = "Responsável pelo gerenciamento de receitas")
interface ReceitaApi {

    @PostMapping
    fun cadastrar(@RequestBody request: NovaReceitaRequest): ResponseEntity<Unit>

    @GetMapping
    fun listar(pageable: Pageable): Page<ReceitaResponse>

    @GetMapping("/{id}")
    fun detalhar(@PathVariable id: Long): ReceitaResponse
}