package com.inkpot.core.domain.author

import com.inkpot.core.application.port.store.AuthorDto
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import java.util.*

internal class AuthorRepository(domainContext: DomainContext, private val authorStore: AuthorStore) :
    DomainClass(domainContext) {

    fun save(authorAggregate: AuthorAggregate) = authorStore.save(toAuthorDto(authorAggregate))

    fun find(id: UUID) = authorStore.find(id).orElse(null)?.let { toAuthorAggregate(it) }

    fun findAll() = authorStore.findAll().map { toAuthorAggregate(it) }.toSet()

    fun delete(id: UUID) =
        id.also {
            authorStore.find(it).ifPresent(this::deleteItsDocuments)
        }.let { authorStore.delete(it) }

    private fun deleteItsDocuments(author: AuthorDto) =
        author.documentIds.forEach { domainContext.documentRepository().delete(it) }


    private fun toAuthorDto(aggregate: AuthorAggregate) = AuthorDto(aggregate.id, aggregate.name, aggregate.documentIds)
    private fun toAuthorAggregate(authorDto: AuthorDto) =
        AggregateBuilder(domainContext)
            .id(authorDto.id)
            .name(authorDto.name)
            .documentIds(authorDto.documentIds)
            .build()

    private class AggregateBuilder(domainContext: DomainContext) :
        AuthorAggregate.Builder, DomainClass(domainContext) {

        private var id: UUID? = null
        private var name: String? = null
        private var documentIds: Set<UUID>? = null

        override fun id() = id!!
        override fun name() = name!!
        override fun documentIds() = documentIds!!

        fun id(id: UUID) = apply { this.id = id }
        fun name(name: String) = apply { this.name = name }
        fun documentIds(documentIds: Set<UUID>) = apply { this.documentIds = documentIds }

        fun build() = AuthorAggregate(domainContext, this)
    }

}
