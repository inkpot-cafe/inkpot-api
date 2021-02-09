package com.inkpot.core.domain.author

import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import java.util.*

internal class AuthorRepository(private val authorStore: AuthorStore) {

    fun save(authorAggregate: AuthorAggregate) = authorStore.save(toAuthorDto(authorAggregate))

    fun find(uuid: UUID) = authorStore.find(uuid).orElse(null)?.let { toAuthorAggregate(it) }

    fun findAll() = authorStore.findAll().map { toAuthorAggregate(it) }.toSet()

    fun delete(uuid: UUID) = authorStore.delete(uuid)

    private fun toAuthorDto(aggregate: AuthorAggregate) = AuthorDto(aggregate.id, aggregate.name)
    private fun toAuthorAggregate(authorDto: AuthorDto) = AggregateBuilder().id(authorDto.id).name(authorDto.name).build()

    private class AggregateBuilder : AuthorAggregate.Builder {

        private var id: UUID? = null
        private var name: String = ""

        override fun id() = id!!
        override fun name() = name

        fun id(id: UUID) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }

        fun build() = AuthorAggregate(this)
    }

}
