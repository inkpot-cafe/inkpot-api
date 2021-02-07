package com.inkpot.core.domain.repository

import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.domain.aggregate.AuthorAggregate
import com.inkpot.core.domain.aggregate.AuthorId
import java.util.*

internal class AuthorRepository(private val authorStore: AuthorStore) {

    fun save(authorAggregate: AuthorAggregate) = authorStore.save(toAuthorDto(authorAggregate))

    fun find(uuid: UUID) = authorStore.find(uuid).orElse(null)?.let { toAuthor(it) }

    fun findAll() = authorStore.findAll().map { toAuthor(it) }.toSet()

    fun delete(uuid: UUID) = authorStore.delete(uuid)

    private fun toAuthorDto(aggregate: AuthorAggregate) = AuthorDto(aggregate.id.uuid, aggregate.name)

    private fun toAuthor(authorDto: AuthorDto) = AuthorAggregate(AuthorId(authorDto.uuid), authorDto.name)
}
