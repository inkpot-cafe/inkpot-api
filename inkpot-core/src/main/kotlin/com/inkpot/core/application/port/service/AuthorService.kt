package com.inkpot.core.application.port.service

import com.inkpot.core.domain.aggregate.AuthorAggregate
import com.inkpot.core.domain.aggregate.AuthorId
import com.inkpot.core.domain.repository.AuthorRepository
import java.util.*

interface AuthorService {
    fun createAuthor(data: AuthorCreateData): Author
    fun findAuthor(uuid: UUID): Optional<Author>
    fun findAllAuthors(): Set<Author>
}

internal class InternalAuthorService(private val authorRepository: AuthorRepository) : AuthorService {

    override fun createAuthor(data: AuthorCreateData) = toAuthor(
        AuthorAggregate(AuthorId(UUID.randomUUID()), data.name).also { authorRepository.save(it) }
    )

    override fun findAuthor(uuid: UUID) = Optional.ofNullable(authorRepository.find(uuid)?.let { toAuthor(it) })
    override fun findAllAuthors() = authorRepository.findAll().map { toAuthor(it) }.toSet()

    private fun toAuthor(aggregate: AuthorAggregate) = Author(aggregate.id.uuid, aggregate.name)

}

data class Author(val uuid: UUID, val name: String)

data class AuthorCreateData(val name: String)