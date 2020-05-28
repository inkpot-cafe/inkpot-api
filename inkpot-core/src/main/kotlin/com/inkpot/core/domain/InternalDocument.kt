package com.inkpot.core.domain

import java.util.*

internal data class InternalDocument(
        private val uuid: UUID,
        private var author: String,
        private var title: String,
        private var content: String
) : Document {
    override fun getUuid() = this.uuid

    override fun getAuthor() = this.author
    override fun setAuthor(author: String) {
        this.author = author
    }

    override fun getTitle() = this.title
    override fun setTitle(title: String) {
        this.title = title
    }

    override fun getContent() = this.content
    override fun setContent(content: String) {
        this.content = content
    }

}



