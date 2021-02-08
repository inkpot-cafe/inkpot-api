package com.inkpot.core.domain.aggregate

import java.util.*

internal data class DocumentAggregate(
    val id: DocumentId,
    var author: String,
    var title: String,
    var content: String
)

internal data class DocumentId(val uuid: UUID)
