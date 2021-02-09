package com.inkpot.core.application.port.service

import com.inkpot.core.domain.author.AuthorAggregate
import com.inkpot.core.domain.author.AuthorAggregateFactory
import com.inkpot.core.domain.author.AuthorRepository
import java.util.*

interface AuthorService {
    fun createAuthor(data: AuthorCreateData): Author
    fun findAuthor(id: UUID): Optional<Author>
    fun findAllAuthors(): Set<Author>
    fun deleteAuthor(id: UUID)
}

internal class InternalAuthorService(
    private val authorAggregateFactory: AuthorAggregateFactory,
    private val authorRepository: AuthorRepository
) : AuthorService {

    override fun createAuthor(data: AuthorCreateData) = toAuthor(
        authorAggregateFactory.newAggregate().name(data.name).build().also { authorRepository.save(it) }
    )

    override fun findAuthor(id: UUID) = Optional.ofNullable(authorRepository.find(id)?.let { toAuthor(it) })

    override fun findAllAuthors() = authorRepository.findAll().map { toAuthor(it) }.toSet()

    override fun deleteAuthor(id: UUID) = authorRepository.delete(id)

    private fun toAuthor(aggregate: AuthorAggregate) = Author(aggregate.id, aggregate.name, aggregate.documentIds)
}

data class Author(val id: UUID, val name: String, val documentIds: Set<UUID>)

data class AuthorCreateData(val name: String)