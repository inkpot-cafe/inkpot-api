package com.inkpot.core.domain

import com.inkpot.core.store.DocumentDto
import com.inkpot.core.store.DocumentStore
import java.util.*

internal class InternalDocumentRepository(
        private val documentStore: DocumentStore
) : DocumentRepository {

    override fun save(document: Document) {
        documentStore.save(toDocumentDto(document))
    }

    override fun find(uuid: UUID): Document? {
        return toDocument(documentStore.find(uuid))
    }

    override fun findAll(): Set<Document> {
        val documents = toDocuments(documentStore.findAll())
        return documents
    }

    override fun delete(uuid: UUID) {
        documentStore.delete(uuid)
    }

    private fun toDocumentDto(document: Document): DocumentDto {
        return DocumentDto(
                document.getUuid(),
                document.getAuthor(),
                document.getTitle(),
                document.getContent()
        )
    }

    private fun toDocument(dto: DocumentDto?): Document? {
        if (dto != null) {
            return InternalDocumentFactory()
                    .uuid(dto.uuid)
                    .title(dto.title)
                    .author(dto.author)
                    .content(dto.content)
                    .create()
        }
        return null
    }

    private fun toDocuments(documentDtoSet: Set<DocumentDto>): Set<Document> {
        return documentDtoSet.asSequence()
                .map {
                    InternalDocumentFactory()
                            .uuid(it.uuid)
                            .title(it.title)
                            .author(it.author)
                            .content(it.content)
                            .create()
                }
                .toSet()
    }

}
