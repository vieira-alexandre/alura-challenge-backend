package dev.alexandrevieira.alurachallengebackend.service

import dev.alexandrevieira.alurachallengebackend.exception.NotFoundException
import dev.alexandrevieira.alurachallengebackend.exception.UnprocessableEntityException
import dev.alexandrevieira.alurachallengebackend.model.entities.Receita
import dev.alexandrevieira.alurachallengebackend.model.repositories.ReceitaRepository
import dev.alexandrevieira.alurachallengebackend.util.Mensagens
import org.springframework.beans.BeanUtils
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.YearMonth
import java.util.*

@Service
class ReceitaService(
    val repository: ReceitaRepository,
) {
    fun cadastrar(receita: Receita): Receita {
        return repository.save(receita)
    }

    fun listar(pageable: Pageable): Page<Receita> {
        return repository.findAll(pageable)
    }

    fun detalhar(id: Long): Receita {
        return repository.findById(id).orElseThrow {
            throw NotFoundException(Receita::class)
        }
    }

    fun listarPorMes(pageable: Pageable, mes: YearMonth): Page<Receita> {
        return repository.findByMes(pageable, mes)
    }

    fun excluir(id: Long) {
        if (repository.existsById(id))
            repository.deleteById(id)
        else
            throw NotFoundException(Receita::class)
    }

    fun atualizar(id: Long, request: Receita) {
        val receitaOptional: Optional<Receita> = repository.findById(id)

        if (receitaOptional.isEmpty)
            throw NotFoundException(Receita::class)

        val carregada: Receita = receitaOptional.get()

        //se estiver alterando o mes/descrição do elemento para o mês/descrição de outro já existente
        if (estaAlterando(carregada, request) && repository.existsByDescricaoAndMes(request.descricao, request.mes))
            throw UnprocessableEntityException(Mensagens.receitaJaCadastrada)

        BeanUtils.copyProperties(request, receitaOptional.get(), "id")
        repository.save(receitaOptional.get())
    }

    private fun estaAlterando(receita1: Receita, receita2: Receita): Boolean {
        return receita1.mes != receita2.mes || receita1.descricao != receita2.descricao
    }

    fun listarContendo(pageable: Pageable, descricao: String): Page<Receita> {
        return repository.findByDescricaoContainingIgnoreCase(pageable, descricao)
    }
}