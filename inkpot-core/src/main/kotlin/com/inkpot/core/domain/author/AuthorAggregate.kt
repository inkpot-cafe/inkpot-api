package com.inkpot.core.domain.author

import java.util.*

internal class AuthorAggregate(builder: Builder) {

    val id = builder.id()
    var name = builder.name()
        private set

    interface Builder {
        fun id(): UUID
        fun name(): String
    }

}
