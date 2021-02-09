package com.inkpot.core.domain.document

import com.inkpot.core.domain.author.AuthorRepository
import java.util.*

internal class DocumentAggregateFactory(private val authorRepository: AuthorRepository) {

    fun newAggregate(): DocumentAggregateBuilder = DocumentAggregateBuilder(authorRepository)

    internal class DocumentAggregateBuilder(private val authorRepository: AuthorRepository) :
        DocumentAggregate.Builder {

        private var id: UUID? = null
        private var authorId: UUID? = null
        private var title: String = ""
        private var content: String = ""

        override fun id() = id!!
        override fun authorId() = authorId!!
        override fun title() = title
        override fun content() = content

        fun authorId(authorId: UUID) = apply { this.authorId = authorId }
        fun title(title: String) = apply { this.title = title }
        fun content(content: String) = apply { this.content = content }

        fun build(): DocumentAggregate {
            checkNotNull(authorId) { "authorId must be not null" }
            checkNotNull(authorId?.let { authorRepository.find(it) }) { "No Author found with authorId: $authorId" }
            id = UUID.randomUUID()
            return DocumentAggregate(this)
        }
    }

}