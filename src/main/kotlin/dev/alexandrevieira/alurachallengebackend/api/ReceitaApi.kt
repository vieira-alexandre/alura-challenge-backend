package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ReceitaResponse
import dev.alexandrevieira.alurachallengebackend.validation.ReceitaUnique
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RequestMapping("/receitas")
@Validated
@Api(value = "receita-api", tags = ["Receitas API"], description = "Responsável pelo gerenciamento de receitas")
interface ReceitaApi {

    @PostMapping
    fun cadastrar(@RequestBody @Valid @ReceitaUnique request: NovaReceitaRequest): ResponseEntity<Unit>

    @GetMapping
    fun listar(pageable: Pageable): Page<ReceitaResponse>

    @GetMapping("/{id}")
    fun detalhar(@PathVariable id: Long): ReceitaResponse

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun excluir(@PathVariable id: Long)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizar(@PathVariable id: Long, @RequestBody @Valid request: NovaReceitaRequest)
}