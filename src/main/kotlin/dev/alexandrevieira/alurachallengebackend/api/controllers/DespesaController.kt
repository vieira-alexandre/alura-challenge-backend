package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.DespesaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
class DespesaController(
    val repository: DespesaRepository,
) : DespesaApi {

    override fun cadastrar(request: NovaDespesaRequest): ResponseEntity<Unit> {
        val despesa: Despesa = request.toModel()
        repository.save(despesa)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(despesa.id).toUri()
        return ResponseEntity.created(uri).build()
    }
}