package com.inkpot.core.domain

import com.inkpot.core.domain.hook.HookHolder
import com.inkpot.core.store.DocumentDto
import com.inkpot.core.store.DocumentStore
import java.util.*

internal class InternalDocumentRepository(
    private val hookHolder: HookHolder,
    private val documentStore: DocumentStore
) : DocumentRepository {

    override fun save(document: Document) {
        hookHolder.executeBefore("save")
        documentStore.save(toDocumentDto(document))
        hookHolder.executeAfter("save")
    }

    override fun find(uuid: UUID): Document? {
        hookHolder.executeBefore("find")
        val document = toDocument(documentStore.find(uuid))
        hookHolder.executeAfter("find")
        return document
    }

    override fun findAll(): Set<Document> {
        hookHolder.executeBefore("findAll")
        val documents = toDocuments(documentStore.findAll())
        hookHolder.executeAfter("findAll")
        return documents
    }

    override fun delete(uuid: UUID) {
        hookHolder.executeBefore("delete")
        documentStore.delete(uuid)
        hookHolder.executeAfter("delete")
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
