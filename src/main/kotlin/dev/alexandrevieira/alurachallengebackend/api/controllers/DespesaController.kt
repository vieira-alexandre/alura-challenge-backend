package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.DespesaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.DespesaResponse
import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*

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

    override fun listar(pageable: Pageable): Page<DespesaResponse> {
        return repository.findAll(pageable).map { DespesaResponse.of(it) }
    }

    override fun detalhar(id: Long): DespesaResponse {
        val despesaOptional: Optional<Despesa> = repository.findById(id)
        if (despesaOptional.isEmpty) throw NotFoundException(Despesa::class)
        return DespesaResponse.of(despesaOptional.get())
    }

    override fun excluir(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw NotFoundException(Despesa::class)
    }

    override fun atualizar(id: Long, request: NovaDespesaRequest) {
        val despesaOptional: Optional<Despesa> = repository.findById(id)
        if (despesaOptional.isEmpty) throw NotFoundException(Despesa::class)
        val carregada: Despesa = despesaOptional.get()
        BeanUtils.copyProperties(request, carregada, "id")
        repository.save(carregada)
    }
}