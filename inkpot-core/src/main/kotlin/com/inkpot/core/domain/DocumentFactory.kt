package com.inkpot.core.domain

import java.util.*

interface DocumentFactory {
    fun uuid(uuid: UUID): DocumentFactory
    fun author(author: String): DocumentFactory
    fun title(title: String): DocumentFactory
    fun content(content: String): DocumentFactory
    fun create(): Document
}