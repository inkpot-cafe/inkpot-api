package com.inkpot.core.domain.document

import com.inkpot.core.domain.author.AuthorRepository
import java.util.*

internal class DocumentAggregate private constructor(
    id: UUID, authorId: UUID, title: String, content: String
) {
    val id: UUID = id
    var authorId: UUID = authorId
        private set
    var title: String = title
        private set
    var content: String = content
        private set

    constructor(builder: Factory.Builder) : this(
        builder.id!!,
        builder.authorId!!,
        builder.title,
        builder.content
    )

    constructor(builder: DocumentRepository.Builder) : this(
        builder.id!!,
        builder.authorId!!,
        builder.title,
        builder.content
    )

    internal class Factory(private val authorRepository: AuthorRepository) {

        fun newAggregate() = Builder(authorRepository)

        internal class Builder(
            private val authorRepository: AuthorRepository
        ) {
            var id: UUID? = null
                private set
            var authorId: UUID? = null
                private set
            var title: String = ""
                private set
            var content: String = ""
                private set

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
}

