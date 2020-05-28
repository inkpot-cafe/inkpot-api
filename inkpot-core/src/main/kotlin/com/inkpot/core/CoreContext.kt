package com.inkpot.core

import com.inkpot.core.domain.DocumentFactory
import com.inkpot.core.domain.DocumentRepository
import com.inkpot.core.domain.hook.Hook

interface CoreContext {
    fun documentRepository(): DocumentRepository
    fun documentFactory(): DocumentFactory
    fun addHookBefore(name: String, hook: Hook)
    fun addHookAfter(name: String, hook: Hook)
}