package com.inkpot.core.application.port.store

import java.util.*

interface AuthorStore {
    fun save(authorDto: AuthorDto)
}

data class AuthorDto(val uuid: UUID, val name: String)