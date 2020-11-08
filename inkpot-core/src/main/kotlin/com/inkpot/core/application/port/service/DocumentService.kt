package com.inkpot.core.application.port.service

import com.inkpot.core.domain.aggregate.DocumentAggregate
import com.inkpot.core.domain.aggregate.DocumentId
import com.inkpot.core.domain.aggregate.DocumentRepository
import java.util.*

class DocumentService internal constructor(private val documentRepository: DocumentRepository) {

    fun createDocument(createDocument: CreateDocument): Document {
        val documentAggregate = DocumentAggregate(DocumentId(UUID.randomUUID()), createDocument.author, createDocument.title, createDocument.content)
        documentRepository.save(documentAggregate)
        return Document(documentAggregate.id.uuid, documentAggregate.author, documentAggregate.title, documentAggregate.content)
    }

    fun findDocument(uuid: UUID): Document? {
        val documentAggregate = documentRepository.find(uuid)
        if (documentAggregate != null)
            return Document(documentAggregate.id.uuid, documentAggregate.author, documentAggregate.title, documentAggregate.content)
        return null
    }

    fun findAllDocuments(): Set<Document> {
        val documentSet = documentRepository.findAll()
        return documentSet.map { Document(it.id.uuid, it.author, it.title, it.content) }.toSet()
    }

    fun deleteDocument(uuid: UUID) {
        documentRepository.delete(uuid)
    }

}

data class CreateDocument(
        var author: String,
        var title: String,
        var content: String
)

data class Document(val uuid: UUID,
                    val author: String,
                    val title: String,
                    val content: String
)

