package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.ReceitaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class ReceitaController(
    val repository: ReceitaRepository,
) : ReceitaApi {

    override fun cadastrar(request: NovaReceitaRequest): ResponseEntity<Unit> {
        val receita: Receita = request.toModel()
        repository.save(receita)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(receita.id).toUri()
        return ResponseEntity.created(uri).build()
    }
}