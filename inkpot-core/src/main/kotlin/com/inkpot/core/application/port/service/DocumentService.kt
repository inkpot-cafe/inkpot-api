package com.inkpot.core.application.port.service

import com.inkpot.core.domain.aggregate.DocumentAggregate
import com.inkpot.core.domain.aggregate.DocumentId
import com.inkpot.core.domain.aggregate.DocumentRepository
import java.util.*


interface DocumentService {
    fun createDocument(createDocument: CreateDocument): Document
    fun findDocument(uuid: UUID): Optional<Document>
    fun findAllDocuments(): Set<Document>
    fun deleteDocument(uuid: UUID)
}

internal class InternalDocumentService internal constructor(private val documentRepository: DocumentRepository) :
    DocumentService {

    override fun createDocument(createDocument: CreateDocument): Document {
        val documentAggregate = DocumentAggregate(
            DocumentId(UUID.randomUUID()),
            createDocument.author,
            createDocument.title,
            createDocument.content
        )
        documentRepository.save(documentAggregate)
        return toDocument(documentAggregate)
    }

    override fun findDocument(uuid: UUID): Optional<Document> {
        val documentAggregate = documentRepository.find(uuid)
        return Optional.ofNullable(documentAggregate?.let { toDocument(it) })
    }

    override fun findAllDocuments(): Set<Document> {
        val documentSet = documentRepository.findAll()
        return documentSet.map { Document(it.id.uuid, it.author, it.title, it.content) }.toSet()
    }

    override fun deleteDocument(uuid: UUID) {
        documentRepository.delete(uuid)
    }

}

private fun toDocument(documentAggregate: DocumentAggregate): Document {
    return Document(
        documentAggregate.id.uuid,
        documentAggregate.author,
        documentAggregate.title,
        documentAggregate.content
    )
}

data class CreateDocument(
    var author: String,
    var title: String,
    var content: String
)

data class Document(
    val uuid: UUID,
    val author: String,
    val title: String,
    val content: String
)
