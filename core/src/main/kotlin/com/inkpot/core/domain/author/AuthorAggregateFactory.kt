package com.inkpot.core.domain.author

import com.inkpot.core.domain.DomainClass
import com.inkpot.core.domain.DomainContext
import java.util.*

internal class AuthorAggregateFactory(domainContext: DomainContext) : DomainClass(domainContext) {

    fun newAggregate() = AuthorAggregateBuilder(domainContext)

    internal class AuthorAggregateBuilder(domainContext: DomainContext) :
        AuthorAggregate.Builder, DomainClass(domainContext) {

        private val id: UUID = UUID.randomUUID()
        private var name: String = ""
        private val documentIds: Set<UUID> = emptySet()

        override fun id() = id
        override fun name() = name
        override fun documentIds() = documentIds

        fun name(name: String) = apply { this.name = name }

        fun build(): AuthorAggregate {
            return AuthorAggregate(domainContext, this)
        }
    }

}