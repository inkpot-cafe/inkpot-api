package com.inkpot.core.application.port.store

import java.util.*

interface AuthorStore {
    fun save(authorDto: AuthorDto)
    fun find(uuid: UUID): Optional<AuthorDto>
}

data class AuthorDto(val uuid: UUID, val name: String)