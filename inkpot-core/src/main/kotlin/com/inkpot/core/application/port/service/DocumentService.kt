package com.inkpot.core.application.port.service

import com.inkpot.core.domain.document.DocumentAggregate
import com.inkpot.core.domain.document.DocumentRepository
import java.util.*


interface DocumentService {
    fun createDocument(data: DocumentCreateData): Document
    fun findDocument(id: UUID): Optional<Document>
    fun findAllDocuments(): Set<Document>
    fun deleteDocument(id: UUID)
}

internal class InternalDocumentService(
    private val documentFactory: DocumentAggregate.Factory,
    private val documentRepository: DocumentRepository
) :
    DocumentService {

    override fun createDocument(data: DocumentCreateData) = toDocument(
        documentFactory.newAggregate()
            .authorId(data.authorId)
            .title(data.title)
            .content(data.content)
            .build()
            .also {
                documentRepository.save(it)
            }
    )

    override fun findDocument(id: UUID) = Optional.ofNullable(documentRepository.find(id)?.let { toDocument(it) })

    override fun findAllDocuments() = documentRepository.findAll().map { toDocument(it) }.toSet()

    override fun deleteDocument(id: UUID) = documentRepository.delete(id)

    private fun toDocument(documentAggregate: DocumentAggregate) =
        Document(
            documentAggregate.id,
            documentAggregate.authorId,
            documentAggregate.title,
            documentAggregate.content
        )

}

data class DocumentCreateData(
    var authorId: UUID,
    var title: String,
    var content: String
)

data class Document(
    val id: UUID,
    val authorId: UUID,
    val title: String,
    val content: String
)
