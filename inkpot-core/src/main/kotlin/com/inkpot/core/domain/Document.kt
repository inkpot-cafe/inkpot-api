package com.inkpot.core.domain

import java.util.*

interface Document {
    fun getUuid(): UUID
    fun getAuthor(): String
    fun setAuthor(author: String)
    fun getTitle(): String
    fun setTitle(title: String)
    fun getContent(): String
    fun setContent(content: String)
}