package com.inkpot.core.application.port.store

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import com.inkpot.core.domain.author.AuthorAggregate
import com.inkpot.core.domain.author.AuthorRepository
import java.util.*

interface AuthorDao {
    fun save(authorDto: AuthorDto)
    fun find(uuid: UUID): Optional<AuthorDto>
    fun findAll(): Set<AuthorDto>
    fun delete(uuid: UUID)
}

data class AuthorDto(val id: UUID, val name: String, val documentIds: Set<UUID>)

internal class InternalAuthorRepository(domainContext: DomainContext, private val authorDao: AuthorDao) :
    DomainClass(domainContext), AuthorRepository {

    override fun save(authorAggregate: AuthorAggregate) = authorDao.save(toAuthorDto(authorAggregate))

    override fun find(id: UUID) = authorDao.find(id).orElse(null)?.let { toAuthorAggregate(it) }

    override fun findAll() = authorDao.findAll().map { toAuthorAggregate(it) }.toSet()

    override fun delete(id: UUID) =
        id.also {
            authorDao.find(it).ifPresent(this::deleteItsDocuments)
        }.let { authorDao.delete(it) }

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