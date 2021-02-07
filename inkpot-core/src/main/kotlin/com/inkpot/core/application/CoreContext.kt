package com.inkpot.core.application

import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.service.InternalDocumentService
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.aggregate.DocumentRepository

interface CoreContext {
    fun documentService(): DocumentService
}
internal class InternalCoreContext internal constructor(documentStore: DocumentStore) : CoreContext {

    private val documentRepository: DocumentRepository = DocumentRepository(documentStore)

    override fun documentService(): DocumentService = InternalDocumentService(documentRepository)

}
