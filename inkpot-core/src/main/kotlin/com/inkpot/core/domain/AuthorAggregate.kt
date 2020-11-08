package com.inkpot.core.domain

import java.util.*

internal data class AuthorAggregate(
        val uuid: UUID,
        var name: String)