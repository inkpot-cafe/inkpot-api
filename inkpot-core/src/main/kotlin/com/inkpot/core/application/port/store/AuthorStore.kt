package com.inkpot.core.application.port.store

import java.util.*

interface AuthorStore {
    fun save(authorDto: AuthorDto)
    fun find(uuid: UUID): Optional<AuthorDto>
    fun findAll(): Set<AuthorDto>
    fun delete(uuid: UUID)
}

data class AuthorDto(val id: UUID, val name: String)