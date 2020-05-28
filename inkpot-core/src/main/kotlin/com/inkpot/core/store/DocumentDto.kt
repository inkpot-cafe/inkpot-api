package com.inkpot.core.store

import java.util.*

data class DocumentDto(
    val uuid: UUID,
    val author: String,
    val title: String,
    val content: String
)