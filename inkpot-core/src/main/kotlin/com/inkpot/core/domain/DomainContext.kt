package com.inkpot.core.domain

import com.inkpot.core.domain.author.AuthorAggregateFactory
import com.inkpot.core.domain.author.AuthorRepository
import com.inkpot.core.domain.document.DocumentAggregateFactory
import com.inkpot.core.domain.document.DocumentRepository

internal interface DomainContext {
    fun authorRepository(): AuthorRepository
    fun documentRepository(): DocumentRepository

    fun authorAggregateFactory(): AuthorAggregateFactory
    fun documentAggregateFactory(): DocumentAggregateFactory
}

internal abstract class DomainClass(protected val domainContext: DomainContext)