package com.inkpot.core.application.port.service

import com.inkpot.core.domain.aggregate.AuthorAggregate
import com.inkpot.core.domain.aggregate.AuthorId
import com.inkpot.core.domain.repository.AuthorRepository
import java.util.*

interface AuthorService {
    fun createAuthor(data: AuthorCreateData): Author
}

internal class InternalAuthorService(private val authorRepository: AuthorRepository) : AuthorService {

    override fun createAuthor(data: AuthorCreateData): Author {
        val authorAggregate = AuthorAggregate(AuthorId(UUID.randomUUID()), data.name)
        authorRepository.save(authorAggregate)
        return toAuthor(authorAggregate)
    }

    private fun toAuthor(aggregate: AuthorAggregate): Author {
        return Author(aggregate.id.uuid, aggregate.name)
    }
}

data class Author(val uuid: UUID, val name: String)

data class AuthorCreateData(val name: String)