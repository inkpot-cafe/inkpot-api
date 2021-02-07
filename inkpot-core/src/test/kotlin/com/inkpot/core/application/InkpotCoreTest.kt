package com.inkpot.core.application

import com.nhaarman.mockitokotlin2.mock
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class InkpotCoreTest {

    @Test
    internal fun `create Context`() {
        val context = InkpotCore.createContext(mock(), mock())
        assertNotNull(context)
        assertNotNull(context.documentService())
        assertNotNull(context.authorService())
    }

}



