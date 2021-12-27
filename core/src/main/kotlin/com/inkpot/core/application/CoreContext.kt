package com.inkpot.core.application

import com.inkpot.core.application.port.service.AuthorService
import com.inkpot.core.application.port.service.DocumentService
import com.inkpot.core.application.port.service.InternalAuthorService
import com.inkpot.core.application.port.service.InternalDocumentService
import com.inkpot.core.application.port.store.AuthorDao
import com.inkpot.core.application.port.store.DocumentDao
import com.inkpot.core.domain.DomainContext
import com.inkpot.core.domain.author.AuthorAggregateFactory
import com.inkpot.core.domain.author.AuthorRepository
import com.inkpot.core.application.port.store.InternalAuthorRepository
import com.inkpot.core.domain.document.DocumentAggregateFactory
import com.inkpot.core.domain.document.DocumentRepository
import com.inkpot.core.application.port.store.InternalDocumentRepository

interface CoreContext {
    fun documentService(): DocumentService
    fun authorService(): AuthorService
}

internal class InternalCoreContext(authorDao: AuthorDao, documentDao: DocumentDao) :
    CoreContext, DomainContext {

    private val authorRepository: AuthorRepository = InternalAuthorRepository(this, authorDao)
    private val documentRepository: DocumentRepository = InternalDocumentRepository(this, documentDao)

    private val authorAggregateFactory = AuthorAggregateFactory(this)
    private val documentAggregateFactory = DocumentAggregateFactory(this)

    override fun documentService() = InternalDocumentService(documentAggregateFactory, documentRepository)
    override fun authorService() = InternalAuthorService(authorAggregateFactory, authorRepository)

    override fun authorRepository() = authorRepository
    override fun documentRepository() = documentRepository

    override fun authorAggregateFactory() = authorAggregateFactory
    override fun documentAggregateFactory() = documentAggregateFactory
}
