package com.inkpot.core.domain.document

import com.inkpot.core.application.port.store.DocumentDto
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import java.util.*

internal class DocumentRepository(domainContext: DomainContext, private val documentStore: DocumentStore) :
    DomainClass(domainContext) {

    fun save(document: DocumentAggregate) = documentStore.save(toDocumentDto(document))

    fun find(id: UUID): DocumentAggregate? =
        documentStore.find(id).orElse(null)?.let { toDocumentAggregate(it) }

    fun findAll(): Set<DocumentAggregate> = documentStore.findAll().map { toDocumentAggregate(it) }.toSet()

    fun delete(id: UUID) = documentStore.delete(id)

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
