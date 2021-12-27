package com.inkpot.core.domain.author

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import java.util.*

internal class AuthorAggregate(domainContext: DomainContext, builder: Builder) : DomainClass(domainContext) {
    val id = builder.id()
    var name = builder.name()
        private set
    val documentIds = builder.documentIds()

    interface Builder {
        fun id(): UUID
        fun name(): String
        fun documentIds(): Set<UUID>
    }
}
