package com.inkpot.core.domain

import com.inkpot.core.AUTHOR
import com.inkpot.core.CONTENT
import com.inkpot.core.RANDOM_UUID
import com.inkpot.core.TITLE
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

internal class DocumentTest {

    @Test
    internal fun builder() {
        val builder = InternalDocumentFactory()
        val document = builder
            .author(AUTHOR)
            .title(TITLE)
            .uuid(RANDOM_UUID)
            .content(CONTENT)
            .create()

        assertEquals(AUTHOR, document.getAuthor())
        assertEquals(TITLE, document.getTitle())
        assertEquals(RANDOM_UUID, document.getUuid())
        assertEquals(CONTENT, document.getContent())
    }

    @Test
    internal fun `default builder`() {
        val builder = InternalDocumentFactory()
        val document = builder.create()

        assertNotNull(document.getUuid())
        assertEquals("", document.getTitle())
        assertEquals("", document.getAuthor())
        assertEquals("", document.getContent())
    }

    @Test
    internal fun setAuthor() {
        val document = InternalDocumentFactory().create()

        document.setAuthor(AUTHOR)

        assertEquals(AUTHOR, document.getAuthor())
    }

    @Test
    internal fun setTitle() {
        val document = InternalDocumentFactory().create()

        document.setTitle(TITLE)

        assertEquals(TITLE, document.getTitle())
    }

    @Test
    internal fun setContent() {
        val document = InternalDocumentFactory().create()

        document.setContent(CONTENT)

        assertEquals(CONTENT, document.getContent())
    }
}