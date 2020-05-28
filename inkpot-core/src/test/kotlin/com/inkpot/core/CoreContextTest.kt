package com.inkpot.core

import com.inkpot.core.domain.hook.Hook
import com.inkpot.core.store.DocumentStore
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CoreContextTest {

    lateinit var store: DocumentStore
    lateinit var coreContext: CoreContext

    @BeforeEach
    internal fun setUp() {
        store = mock()
        coreContext = InternalCoreContext(store)
    }

    @Test
    internal fun documentBuilder() {
        val documentBuilder = coreContext.documentFactory()
        assertNotNull(documentBuilder)
    }

    @Test
    internal fun documentRepository() {
        val documentRepository = coreContext.documentRepository()
        assertNotNull(documentRepository)
    }

    @Test
    internal fun addHookBefore() {
        val hook: Hook = mock()
        coreContext.addHookBefore("findAll", hook)

        coreContext.documentRepository().findAll()

        verify(hook).execute()
    }

    @Test
    internal fun addHookAfter() {
        val hook: Hook = mock()
        coreContext.addHookAfter("findAll", hook)

        coreContext.documentRepository().findAll()

        verify(hook).execute()
    }
}