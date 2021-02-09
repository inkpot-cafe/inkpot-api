package com.inkpot.core.domain.document

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import com.inkpot.core.domain.author.AuthorRepository
import java.util.*

internal class DocumentAggregateFactory(domainContext: DomainContext) :
    DomainClass(domainContext) {

    fun newAggregate() = DocumentAggregateBuilder(domainContext)

    internal class DocumentAggregateBuilder(
        domainContext: DomainContext
    ) : DocumentAggregate.Builder, DomainClass(domainContext) {

        private val id: UUID = UUID.randomUUID()
        private var authorId: UUID? = null
        private var title: String = ""
        private var content: String = ""

        override fun id() = id
        override fun authorId() = authorId!!
        override fun title() = title
        override fun content() = content

        fun authorId(authorId: UUID) = apply { this.authorId = authorId }
        fun title(title: String) = apply { this.title = title }
        fun content(content: String) = apply { this.content = content }

        fun build(): DocumentAggregate {
            checkNotNull(authorId) { "authorId must be not null" }
            checkNotNull(
                authorId?.let { domainContext.authorRepository().find(it) }
            ) { "No Author found with authorId: $authorId" }

            return DocumentAggregate(domainContext, this)
        }
    }

}