package com.inkpot.core.application

import com.inkpot.core.application.port.store.DocumentStore
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class InternalCoreContextTest {

    lateinit var store: DocumentStore
    lateinit var coreContext: InternalCoreContext

    @BeforeEach
    internal fun setUp() {
        store = mock()
        coreContext = InternalCoreContext(store)
    }

    @Test
    internal fun documentService() {
        val documentService = coreContext.documentService()
        assertNotNull(documentService)
    }

}