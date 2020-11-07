package com.inkpot.core.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class AuthorTest {

    @Test
    internal fun factory() {
        val factory = InternalAuthorFactory()

        val author = factory.name("name")
                .create()

        assertEquals("name", author.getName())
        assertNotNull(author.getUuid())
    }

    @Test
    internal fun setName() {
        val author = InternalAuthorFactory().create()

        author.setName("name")

        assertEquals("name", author.getName())
    }

}