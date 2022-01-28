package dev.alexandrevieira.alurachallengebackend.api

import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.DespesaResponse
import dev.alexandrevieira.alurachallengebackend.validation.DespesaUnique
import io.swagger.annotations.Api
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import javax.validation.constraints.Max
import javax.validation.constraints.Min

@RequestMapping("/despesas")
@Validated
@Api(value = "despesa-api", tags = ["Despesas API"], description = "Responsável pelo gerenciamento de despesas")
interface DespesaApi {
    @PostMapping
    fun cadastrar(@RequestBody @Valid @DespesaUnique request: NovaDespesaRequest): ResponseEntity<Unit>

    @GetMapping
    fun listar(pageable: Pageable, @RequestParam descricao: String?): Page<DespesaResponse>

    @GetMapping("/{id}")
    fun detalhar(@PathVariable id: Long): DespesaResponse

    @GetMapping("/{ano}/{mes}")
    fun listarPorMes(
        pageable: Pageable,
        @PathVariable @Min(2022) @Max(2100) ano: Int,
        @PathVariable @Min(1) @Max(12) mes: Int,
    ): Page<DespesaResponse>

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun excluir(@PathVariable id: Long)

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun atualizar(@PathVariable id: Long, @RequestBody @Valid request: NovaDespesaRequest)
}