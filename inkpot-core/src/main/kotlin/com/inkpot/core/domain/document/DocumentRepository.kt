package com.inkpot.core.domain.document

import com.inkpot.core.application.port.store.DocumentDto
import com.inkpot.core.application.port.store.DocumentStore
import java.util.*

internal class DocumentRepository(
    private val documentStore: DocumentStore
) {

    fun save(document: DocumentAggregate) = documentStore.save(toDocumentDto(document))

    fun find(uuid: UUID): DocumentAggregate? =
        documentStore.find(uuid).orElse(null)?.let { toDocument(it) }

    fun findAll(): Set<DocumentAggregate> = documentStore.findAll().map { toDocument(it) }.toSet()

    fun delete(uuid: UUID) = documentStore.delete(uuid)

    private fun toDocumentDto(document: DocumentAggregate) =
        DocumentDto(
            document.id,
            document.authorId,
            document.title,
            document.content
        )

    private fun toDocument(dto: DocumentDto) =
        AggregateBuilder()
            .id(dto.id)
            .authorId(dto.authorId)
            .title(dto.title)
            .content(dto.content)
            .build()

    private class AggregateBuilder : DocumentAggregate.Builder {
        var id: UUID? = null
            private set
        var authorId: UUID? = null
            private set
        var title: String = ""
            private set
        var content: String = ""
            private set

        override fun id() = id!!
        override fun authorId() = authorId!!
        override fun title() = title
        override fun content() = content

        fun id(id: UUID) = apply { this.id = id }
        fun authorId(authorId: UUID) = apply { this.authorId = authorId }
        fun title(title: String) = apply { this.title = title }
        fun content(content: String) = apply { this.content = content }

        fun build(): DocumentAggregate {
            return DocumentAggregate(this)
        }
    }

}
