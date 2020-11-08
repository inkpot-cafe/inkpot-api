package com.inkpot.core.application

import com.inkpot.core.application.port.store.DocumentStore
import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class InkpotCoreTest {

    @Test
    internal fun `create Context`() {
        val store: DocumentStore = mock()
        val context = InkpotCore.createContext(store)
        assertNotNull(context)
    }

}



