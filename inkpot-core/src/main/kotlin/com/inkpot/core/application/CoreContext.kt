package com.inkpot.core.application

import com.inkpot.core.application.port.service.AuthorService
import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.service.InternalAuthorService
import com.inkpot.core.application.port.service.InternalDocumentService
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.author.AuthorAggregateFactory
import com.inkpot.core.domain.author.AuthorRepository
import com.inkpot.core.domain.document.DocumentAggregateFactory
import com.inkpot.core.domain.document.DocumentRepository

interface CoreContext {
    fun documentService(): DocumentService
    fun authorService(): AuthorService
}

internal class InternalCoreContext(authorStore: AuthorStore, documentStore: DocumentStore) : CoreContext {

    private val authorRepository = AuthorRepository(authorStore)
    private val documentRepository = DocumentRepository(documentStore)

    private val authorAggregateFactory = AuthorAggregateFactory()
    private val documentAggregateFactory = DocumentAggregateFactory(authorRepository)

    override fun documentService() = InternalDocumentService(documentAggregateFactory, documentRepository)
    override fun authorService() = InternalAuthorService(authorAggregateFactory, authorRepository)
}
