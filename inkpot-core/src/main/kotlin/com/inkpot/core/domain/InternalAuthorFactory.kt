package com.inkpot.core.domain

import java.util.*

internal class InternalAuthorFactory {

    private val uuid = UUID.randomUUID()
    private var name = ""

    fun name(name: String) = apply { this.name = name }

    fun create(): Author {
        return InternalAuthor(uuid, name)
    }

}
