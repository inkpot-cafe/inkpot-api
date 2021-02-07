package com.inkpot.core.application.port.store

import java.util.*

interface DocumentStore {
    fun save(document: DocumentDto)
    fun find(uuid: UUID): Optional<DocumentDto>
    fun findAll(): Set<DocumentDto>
    fun delete(uuid: UUID)
}

data class DocumentDto(
        val uuid: UUID,
        val author: String,
        val title: String,
        val content: String
)
