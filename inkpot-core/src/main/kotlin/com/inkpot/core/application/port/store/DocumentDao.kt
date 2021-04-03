package com.inkpot.core.application.port.store

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import com.inkpot.core.domain.document.DocumentAggregate
import com.inkpot.core.domain.document.DocumentRepository
import java.util.*

interface DocumentDao {
    fun save(document: DocumentDto)
    fun find(uuid: UUID): Optional<DocumentDto>
    fun findAll(): Set<DocumentDto>
    fun delete(uuid: UUID)
}

data class DocumentDto(
    val id: UUID,
    val authorId: UUID,
    val title: String,
    val content: String
)

internal class InternalDocumentRepository(domainContext: DomainContext, private val documentDao: DocumentDao) :
    DomainClass(domainContext), DocumentRepository {
    override fun save(document: DocumentAggregate) = documentDao.save(toDocumentDto(document))

    override fun find(id: UUID): DocumentAggregate? =
        documentDao.find(id).orElse(null)?.let { toDocumentAggregate(it) }

    override fun findAll(): Set<DocumentAggregate> = documentDao.findAll().map { toDocumentAggregate(it) }.toSet()

    override fun delete(id: UUID) = documentDao.delete(id)

    private fun toDocumentDto(document: DocumentAggregate) =
        DocumentDto(
            document.id,
            document.authorId,
            document.title,
            document.content
        )

    private fun toDocumentAggregate(dto: DocumentDto) =
        AggregateBuilder(domainContext)
            .id(dto.id)
            .authorId(dto.authorId)
            .title(dto.title)
            .content(dto.content)
            .build()

    private class AggregateBuilder(domainContext: DomainContext) : DocumentAggregate.Builder,
        DomainClass(domainContext) {
        var id: UUID? = null
            private set
        var authorId: UUID? = null
            private set
        var title: String? = null
            private set
        var content: String? = null
            private set

        override fun id() = id!!
        override fun authorId() = authorId!!
        override fun title() = title!!
        override fun content() = content!!

        fun id(id: UUID) = apply { this.id = id }
        fun authorId(authorId: UUID) = apply { this.authorId = authorId }
        fun title(title: String) = apply { this.title = title }
        fun content(content: String) = apply { this.content = content }

        fun build() = DocumentAggregate(domainContext, this)
    }
}