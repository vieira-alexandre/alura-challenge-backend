package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.ReceitaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ReceitaResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.service.ReceitaService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class ReceitaController(
    val service: ReceitaService,
) : ReceitaApi {

    override fun cadastrar(request: NovaReceitaRequest): ResponseEntity<Unit> {
        var receita: Receita = request.toModel()
        receita = service.cadastrar(receita)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(receita.id).toUri()
        return ResponseEntity.created(uri).build()
    }

    override fun listar(pageable: Pageable): Page<ReceitaResponse> {
        return service.listar(pageable).map { ReceitaResponse.of(it) }
    }

    override fun detalhar(id: Long): ReceitaResponse {
        val receita: Receita = service.detalhar(id)
        return ReceitaResponse.of(receita)
    }

    override fun excluir(id: Long) {
        service.excluir(id)
    }

    override fun atualizar(id: Long, request: NovaReceitaRequest) {
        service.atualizar(id, request.toModel())
    }
}