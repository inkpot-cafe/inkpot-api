package com.inkpot.core.domain.document

import java.util.*

internal interface DocumentRepository {
    fun save(document: DocumentAggregate)
    fun find(id: UUID): DocumentAggregate?
    fun findAll(): Set<DocumentAggregate>
    fun delete(id: UUID)
}

