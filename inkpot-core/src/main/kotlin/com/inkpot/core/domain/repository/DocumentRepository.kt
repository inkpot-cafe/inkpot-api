package com.inkpot.core.domain.repository

import com.inkpot.core.application.port.store.DocumentDto
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.aggregate.DocumentAggregate
import com.inkpot.core.domain.aggregate.DocumentId
import java.util.*

internal class DocumentRepository(
    private val documentStore: DocumentStore
) {

    fun save(document: DocumentAggregate) = documentStore.save(toDocumentDto(document))

    fun find(uuid: UUID): DocumentAggregate? =
        documentStore.find(uuid).orElse(null)?.let { toDocument(it) }

    fun findAll(): Set<DocumentAggregate> = toDocuments(documentStore.findAll())

    fun delete(uuid: UUID) = documentStore.delete(uuid)

    private fun toDocumentDto(document: DocumentAggregate): DocumentDto =
        DocumentDto(
            document.id.uuid,
            document.author,
            document.title,
            document.content
        )

    private fun toDocument(dto: DocumentDto): DocumentAggregate =
        DocumentAggregate(DocumentId(dto.uuid), dto.author, dto.title, dto.content)

    private fun toDocuments(documentDtoSet: Set<DocumentDto>): Set<DocumentAggregate> =
        documentDtoSet.map { toDocument(it) }.toSet()

}
