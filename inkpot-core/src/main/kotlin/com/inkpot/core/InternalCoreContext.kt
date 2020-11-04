package com.inkpot.core

import com.inkpot.core.domain.InternalDocumentFactory
import com.inkpot.core.domain.InternalDocumentRepository
import com.inkpot.core.store.DocumentStore

internal class InternalCoreContext(private val documentStore: DocumentStore) : CoreContext {

    override fun documentRepository() = InternalDocumentRepository(documentStore)

    override fun documentFactory() = InternalDocumentFactory()

}
