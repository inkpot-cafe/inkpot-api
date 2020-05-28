package com.inkpot.core.store

import java.util.*

interface DocumentStore {
    fun save(document: DocumentDto)
    fun find(uuid: UUID): DocumentDto?
    fun findAll(): Set<DocumentDto>
    fun delete(uuid: UUID)
}
