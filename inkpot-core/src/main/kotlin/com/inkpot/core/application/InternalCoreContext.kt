package com.inkpot.core.application

import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.aggregate.DocumentRepository

internal class InternalCoreContext: CoreContext {

    private val documentRepository: DocumentRepository

    internal constructor(documentStore: DocumentStore) {
        documentRepository = DocumentRepository(documentStore)
    }

    override fun documentService(): DocumentService = DocumentService(documentRepository)

}
