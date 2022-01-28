package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.exception.UnprocessableEntityException
import dev.alexandrevieira.alurachallengebackend.model.entities.Despesa
import dev.alexandrevieira.alurachallengebackend.model.repositories.DespesaRepository
import dev.alexandrevieira.alurachallengebackend.util.Mensagens
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class DespesaService(
    val repository: DespesaRepository,
) {
    fun cadastrar(despesa: Despesa): Despesa {
        return repository.save(despesa)
    }

    fun listar(pageable: Pageable): Page<Despesa> {
        return repository.findAll(pageable)
    }

    fun detalhar(id: Long): Despesa {
        return repository.findById(id).orElseThrow { throw NotFoundException(Despesa::class) }
    }

    fun excluir(id: Long) {
        if (repository.existsById(id)) repository.deleteById(id)
        else throw NotFoundException(Despesa::class)
    }

    fun atualizar(id: Long, request: Despesa) {
        val despesaOptional: Optional<Despesa> = repository.findById(id)
        if (despesaOptional.isEmpty) throw NotFoundException(Despesa::class)
        val carregada: Despesa = despesaOptional.get()

        if (estaAlterando(carregada, request) && repository.existsByDescricaoAndMes(request.descricao, request.mes))
            throw UnprocessableEntityException(Mensagens.despesaJaCadastrada)

        BeanUtils.copyProperties(request, despesaOptional.get(), "id")
        repository.save(despesaOptional.get())
    }

    private fun estaAlterando(despesa1: Despesa, despesa2: Despesa): Boolean {
        return despesa1.mes != despesa2.mes || despesa1.descricao != despesa2.descricao
    }

    fun listarContendo(pageable: Pageable, descricao: String): Page<Despesa> {
        return repository.findByDescricaoContainingIgnoreCase(pageable, descricao)
    }
}