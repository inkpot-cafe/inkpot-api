package com.inkpot.core.application.port.service

import com.inkpot.core.domain.aggregate.DocumentAggregate
import com.inkpot.core.domain.aggregate.DocumentId
import com.inkpot.core.domain.repository.DocumentRepository
import java.util.*


interface DocumentService {
    fun createDocument(data: DocumentCreateData): Document
    fun findDocument(uuid: UUID): Optional<Document>
    fun findAllDocuments(): Set<Document>
    fun deleteDocument(uuid: UUID)
}

internal class InternalDocumentService(private val documentRepository: DocumentRepository) :
    DocumentService {

    override fun createDocument(data: DocumentCreateData) = toDocument(
        DocumentAggregate(
            DocumentId(UUID.randomUUID()),
            data.author,
            data.title,
            data.content
        ).also {
            documentRepository.save(it)
        }
    )

    override fun findDocument(uuid: UUID) = Optional.ofNullable(documentRepository.find(uuid)?.let { toDocument(it) })

    override fun findAllDocuments() = documentRepository.findAll().map { toDocument(it) }.toSet()

    override fun deleteDocument(uuid: UUID) = documentRepository.delete(uuid)

    private fun toDocument(documentAggregate: DocumentAggregate) =
        Document(
            documentAggregate.id.uuid,
            documentAggregate.author,
            documentAggregate.title,
            documentAggregate.content
        )

}

data class DocumentCreateData(
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
