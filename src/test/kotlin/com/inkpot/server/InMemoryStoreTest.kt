package com.inkpot.server

import com.inkpot.core.Document
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import java.util.*

internal class InMemoryStoreTest {

    @Test
    internal fun `save and read a Document`() {
        val uuid = UUID.randomUUID()
        val testDocument = TestDocument(uuid)

        InMemoryStore.save(testDocument)

        val loadedDocument = InMemoryStore.load(uuid)

        assertEquals(testDocument, loadedDocument)
    }

    @Test
    internal fun `save and delete a Document`() {
        val uuid = UUID.randomUUID()
        val testDocument = TestDocument(uuid)

        InMemoryStore.save(testDocument)
        InMemoryStore.delete(uuid)

        assertNull(InMemoryStore.load(uuid))
    }

    internal class TestDocument(override val uuid: UUID) : Document {

        override var author = "author"
        override var title = "title"

        override fun save() {
        }

        override fun write(content: String) {
        }

        override fun read(): String {
            return ""
        }

        override fun delete() {
        }
    }
}