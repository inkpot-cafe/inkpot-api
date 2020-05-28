package com.inkpot.core

import com.inkpot.core.domain.InternalDocumentFactory
import com.inkpot.core.domain.InternalDocumentRepository
import com.inkpot.core.domain.hook.Hook
import com.inkpot.core.domain.hook.HookHolder
import com.inkpot.core.store.DocumentStore

internal class InternalCoreContext(private val documentStore: DocumentStore) : CoreContext {

    private val hookHolder = HookHolder()

    override fun documentRepository() = InternalDocumentRepository(hookHolder, documentStore)

    override fun documentFactory() = InternalDocumentFactory()

    override fun addHookBefore(name: String, hook: Hook) {
        hookHolder.addBefore(name, hook)
    }

    override fun addHookAfter(name: String, hook: Hook) {
        hookHolder.addAfter(name, hook)
    }

}
