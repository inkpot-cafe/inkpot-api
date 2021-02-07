package com.inkpot.core.domain.repository

import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.domain.aggregate.AuthorAggregate

internal class AuthorRepository(private val authorStore: AuthorStore) {
    fun save(authorAggregate: AuthorAggregate) {
        authorStore.save(toAuthorDto(authorAggregate))
    }

    private fun toAuthorDto(aggregate: AuthorAggregate): AuthorDto {
        return AuthorDto(aggregate.id.uuid, aggregate.name)
    }

}