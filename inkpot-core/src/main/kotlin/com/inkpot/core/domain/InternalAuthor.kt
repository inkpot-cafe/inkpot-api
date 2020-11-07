package com.inkpot.core.domain

import java.util.*

internal data class InternalAuthor(
        private val uuid: UUID,
        private var name: String) : Author {

    override fun getUuid(): UUID = this.uuid

    override fun setName(name: String) {
        this.name = name
    }

    override fun getName(): String = this.name

}