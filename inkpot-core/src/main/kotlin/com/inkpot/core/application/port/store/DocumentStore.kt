package com.inkpot.core.application.port.store

import java.util.*

interface DocumentStore {
    fun save(document: DocumentDto)
    fun find(uuid: UUID): Optional<DocumentDto>
    fun findAll(): Set<DocumentDto>
    fun delete(uuid: UUID)
}

data class DocumentDto(
    val id: UUID,
    val authorId: UUID,
    val title: String,
    val content: String
)
