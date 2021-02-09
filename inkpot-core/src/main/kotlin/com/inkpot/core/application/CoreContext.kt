package com.inkpot.core.application

import com.inkpot.core.application.port.service.AuthorService
import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.service.InternalAuthorService
import com.inkpot.core.application.port.service.InternalDocumentService
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.author.AuthorRepository
import com.inkpot.core.domain.document.DocumentAggregate
import com.inkpot.core.domain.document.DocumentRepository

interface CoreContext {
    fun documentService(): DocumentService
    fun authorService(): AuthorService
}

internal class InternalCoreContext internal constructor(authorStore: AuthorStore, documentStore: DocumentStore) :
    CoreContext {

    private val authorRepository = AuthorRepository(authorStore)
    private val documentFactory = DocumentAggregate.Factory(authorRepository)
    private val documentRepository = DocumentRepository(documentStore)

    override fun documentService(): DocumentService = InternalDocumentService(documentFactory, documentRepository)
    override fun authorService(): AuthorService = InternalAuthorService(authorRepository)


}
