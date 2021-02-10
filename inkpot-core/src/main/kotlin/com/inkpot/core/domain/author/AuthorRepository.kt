package com.inkpot.core.domain.author

import java.util.*

internal interface AuthorRepository {
    fun save(authorAggregate: AuthorAggregate)
    fun find(id: UUID): AuthorAggregate?
    fun findAll(): Set<AuthorAggregate>
    fun delete(id: UUID)
}

