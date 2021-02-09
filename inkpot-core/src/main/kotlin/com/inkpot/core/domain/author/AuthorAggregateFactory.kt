package com.inkpot.core.domain.author

import java.util.*

internal class AuthorAggregateFactory() {

    fun newAggregate() = AuthorAggregateBuilder()

    internal class AuthorAggregateBuilder() : AuthorAggregate.Builder {

        private var id: UUID? = null
        private var name: String = ""

        override fun id() = id!!
        override fun name() = name

        fun name(name: String) = apply { this.name = name }

        fun build(): AuthorAggregate {
            id = UUID.randomUUID()
            return AuthorAggregate(this)
        }
    }

}