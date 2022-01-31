package dev.alexandrevieira.alurachallengebackend.api.controllers

import dev.alexandrevieira.alurachallengebackend.api.DespesaApi
import dev.alexandrevieira.alurachallengebackend.api.dto.request.NovaDespesaRequest
import dev.alexandrevieira.alurachallengebackend.api.dto.response.DespesaResponse
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.service.DespesaService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.time.YearMonth

@RestController
class DespesaController(
    val service: DespesaService,
) : DespesaApi {

    override fun cadastrar(request: NovaDespesaRequest): ResponseEntity<Unit> {
        var despesa: Despesa = request.toModel()
        despesa = service.cadastrar(despesa)
        val uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(despesa.id).toUri()
        return ResponseEntity.created(uri).build()
    }

    override fun listar(pageable: Pageable, @RequestParam descricao: String?): Page<DespesaResponse> {
        if (!descricao.isNullOrBlank())
            return service.listarContendo(pageable, descricao).map { DespesaResponse.of(it) }

        return service.listar(pageable).map { DespesaResponse.of(it) }
    }

    override fun detalhar(id: Long): DespesaResponse {
        val despesa: Despesa = service.detalhar(id)
        return DespesaResponse.of(despesa)
    }

    override fun listarPorMes(pageable: Pageable, ano: Int, mes: Int): Page<DespesaResponse> {
        val anoMes = YearMonth.of(ano, mes)
        return service.listarPorMes(pageable, anoMes).map { DespesaResponse.of(it) }
    }

    override fun excluir(id: Long) {
        service.excluir(id)
    }

    override fun atualizar(id: Long, request: NovaDespesaRequest) {
        service.atualizar(id, request.toModel())
    }
}