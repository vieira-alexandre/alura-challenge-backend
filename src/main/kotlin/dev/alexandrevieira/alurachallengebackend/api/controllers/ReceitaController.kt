package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.ReceitaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaReceitaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.ReceitaResponse
import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

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

    override fun listar(pageable: Pageable): Page<ReceitaResponse> {
        return repository.findAll(pageable).map { ReceitaResponse.of(it) }
    }

    override fun detalhar(id: Long): ReceitaResponse {
        val receitaOptional = repository.findById(id)
        if (receitaOptional.isEmpty) throw NotFoundException(Receita::class)
        return ReceitaResponse.of(receitaOptional.get())
    }

    override fun excluir(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw NotFoundException(Receita::class)
    }

    override fun atualizar(id: Long, request: NovaReceitaRequest) {
        val receitaOptional: Optional<Receita> = repository.findById(id)
        if (receitaOptional.isEmpty) throw NotFoundException(Receita::class)
        val carregada: Receita = receitaOptional.get()
        BeanUtils.copyProperties(request, carregada, "id")
        repository.save(carregada)
    }
}