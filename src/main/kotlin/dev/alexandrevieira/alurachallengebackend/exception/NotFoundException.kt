package dev.alexandrevieira.alurachallengebackend.exception

import kotlin.reflect.KClass

class NotFoundException(classe: KClass<*>) : RuntimeException("${classe.simpleName} não encontrada") {
}