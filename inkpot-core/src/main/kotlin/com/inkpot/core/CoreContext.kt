package com.inkpot.core

import com.inkpot.core.domain.DocumentFactory
import com.inkpot.core.domain.DocumentRepository

interface CoreContext {
    fun documentRepository(): DocumentRepository
    fun documentFactory(): DocumentFactory
}