package com.inkpot.core.application

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class CoreContextTest {

    lateinit var coreContext: CoreContext

    @BeforeEach
    internal fun setUp() {
        coreContext = InternalCoreContext(mock(), mock())
    }

    @Test
    internal fun documentService() {
        val documentService = coreContext.documentService()
        assertNotNull(documentService)
    }

}
