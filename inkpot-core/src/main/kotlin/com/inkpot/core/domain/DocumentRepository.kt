package com.inkpot.core.domain

import java.util.*

interface DocumentRepository {
    fun save(document: Document)
    fun find(uuid: UUID): Document?
    fun findAll(): Set<Document>
    fun delete(uuid: UUID)
}