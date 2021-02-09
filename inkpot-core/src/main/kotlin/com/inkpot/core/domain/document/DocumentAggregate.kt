package com.inkpot.core.domain.document

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import java.util.*

internal class DocumentAggregate(domainContext: DomainContext, builder: Builder) : DomainClass(domainContext) {
    val id: UUID = builder.id()
    var authorId: UUID = builder.authorId()
        private set
    var title: String = builder.title()
        private set
    var content: String = builder.content()
        private set

    internal interface Builder {
        fun id(): UUID
        fun authorId(): UUID
        fun title(): String
        fun content(): String
    }
}

