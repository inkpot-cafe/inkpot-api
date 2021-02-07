package com.inkpot.core.application

import com.inkpot.core.application.port.service.AuthorService
import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.service.InternalAuthorService
import com.inkpot.core.application.port.service.InternalDocumentService
import com.inkpot.core.application.port.store.AuthorStore
import com.inkpot.core.application.port.store.DocumentStore
import com.inkpot.core.domain.repository.AuthorRepository
import com.inkpot.core.domain.repository.DocumentRepository

interface CoreContext {
    fun documentService(): DocumentService
    fun authorService(): AuthorService
}

internal class InternalCoreContext internal constructor(authorStore: AuthorStore, documentStore: DocumentStore) :
    CoreContext {

    private val documentRepository = DocumentRepository(documentStore)
    private val authorRepository = AuthorRepository(authorStore)

    override fun documentService(): DocumentService = InternalDocumentService(documentRepository)
    override fun authorService(): AuthorService = InternalAuthorService(authorRepository)


}
