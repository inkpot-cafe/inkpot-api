package com.inkpot.core.domain

import java.util.*

internal class InternalDocumentFactory : DocumentFactory {
    private var uuid = UUID.randomUUID()
    private var author = ""
    private var title = ""
    private var content = ""

    override fun uuid(uuid: UUID) = apply { this.uuid = uuid }
    override fun author(author: String) = apply { this.author = author }
    override fun title(title: String) = apply { this.title = title }
    override fun content(content: String) = apply { this.content = content }
    override fun create(): Document = InternalDocument(uuid, author, title, content)
}