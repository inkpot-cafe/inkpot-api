package com.inkpot.core.domain.aggregate

import java.util.*

internal data class AuthorAggregate(
        val id: AuthorId,
        var name: String)

internal data class AuthorId(val uuid: UUID)